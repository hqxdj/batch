package com.ucasp.enter.config;

import com.ucasp.enter.batch.*;
import com.ucasp.enter.entity.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description: 作业配置类
 * @Date 2019/8/26 9:28
 * Copyright (c)   xdj
 */
@Configuration
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FlatMapItemWriter flatMapItemWriter;

    @Autowired
    private BusinessProcessor businessProcessor;

    @Autowired
    private FlowDecider flowDecider;

    @Autowired
    private HandlerDataTasklet handlerDataTasklet;



    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .validator(new DefaultJobParametersValidator())
                .incrementer(new RunIdIncrementer())
                .start(channelFlow())
                .split(threadPoolTaskExecutor())
                .add(bankFlow())
                .next(flowDecider).on("COMPLETED").to(handlerData())
                .end()
                .build();
    }

    // 从渠道文件读取
    @Bean
    public Flow channelFlow() {
        return new FlowBuilder<Flow>("controllerFlow")
                .start(customerFlatFileDemoStep())
                .build();
    }

    // 从银行文件读取
    @Bean
    public Flow bankFlow() {
        return new FlowBuilder<Flow>("controllerFlow")
                .start(flatFileDemoStep())
                .build();
    }

    @Bean
    public Flow fileRead() {
        return new FlowBuilder<Flow>("fileRead")
                .start(handlerData())
                .build();
    }

    // 勾兑
    @Bean
    public Step handlerData() {

        return stepBuilderFactory.get("handlerData")
                .tasklet(handlerDataTasklet)
                .build();
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(0);
        executor.setMaxPoolSize(8);
        executor.setKeepAliveSeconds(4);
        executor.setQueueCapacity(0);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }


    @Bean
    public Step flatFileDemoStep() {
        return stepBuilderFactory.get("flatFileDemoStep")
                .<Customer, Customer>chunk(10)
                .reader(flatFileItemReader())
                .processor(businessProcessor)
                .writer(flatMapItemWriter)
                .faultTolerant()
                .skipLimit(5)
                .skip(FlatFileParseException.class)
                .taskExecutor(threadPoolTaskExecutor())
                .build();
    }

    @Bean
    public Step customerFlatFileDemoStep() {
        return stepBuilderFactory.get("customerFlatFileDemoStep")
                .<Customer, Customer>chunk(10)
                .reader(customerItemReader())
                .processor(businessProcessor)
                .writer(flatMapItemWriter)
                .faultTolerant()
                .skipLimit(5)
                .skip(FlatFileParseException.class)
                .taskExecutor(threadPoolTaskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> flatFileItemReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("/file/first.txt"));
        return getCustomerFlatFileItemReader(reader);
    }

    private FlatFileItemReader<Customer> getCustomerFlatFileItemReader(FlatFileItemReader<Customer> reader) {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        MyDefaultLineMapper tokenizer = new MyDefaultLineMapper();
        tokenizer.setNames("id", "firstName", "lastName", "birthDay");
        tokenizer.setDelimiter("|"); // 设置分隔符
        MyDefaultLineMapper<Customer> lineMapper = new MyDefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet ->
                Customer.builder()
                        .id(fieldSet.readLong("id"))
                        .firstName(fieldSet.readString("firstName"))
                        .lastName(fieldSet.readString("lastName"))
                        .birthDay(fieldSet.readString("birthDay"))
                        .build()
        );
        lineMapper.afterPropertiesSet();
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean("customerItemReader")
    @StepScope
    public FlatFileItemReader<Customer> customerItemReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("/file/second.txt"));
        return getCustomerFlatFileItemReader(reader);
    }




 /*   @Bean
    public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(4);
        return asyncTaskExecutor;
    }
*/

}
