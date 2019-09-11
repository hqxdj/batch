package com.ucasp.enter.config;

import com.ucasp.enter.batch.*;
import com.ucasp.enter.entity.BankFile;
import com.ucasp.enter.entity.ChannelFile;
import com.ucasp.enter.listener.FileCheckJobExecutionListener;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
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
    private ChannelFileItemWriter channelFileItemWriter;

    @Autowired
    private BankFileItemWriter bankFileItemWriter;

    @Autowired
    private ChannelFileProcessor channelFileProcessor;

    @Autowired
    private FlowDecider flowDecider;

    @Autowired
    private HandlerDataTasklet handlerDataTasklet;

    @Autowired
    private FileCheckJobExecutionListener fileCheckJobExecutionListener;


    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .validator(new DefaultJobParametersValidator())
                .incrementer(new RunIdIncrementer())
                .listener(fileCheckJobExecutionListener)
                .start(channelFlow())
                .split(threadPoolTaskExecutor())
                .add(bankFlow())
                .next(flowDecider).on("COMPLETED")
                .to(handlerData())
                .end()
                .build();
    }

    // 从渠道文件读取
    @Bean
    public Flow channelFlow() {
        return new FlowBuilder<Flow>("channelFlow")
                .start(channelFileReadStep())
                .build();
    }

    // 从银行文件读取
    @Bean
    public Flow bankFlow() {
        return new FlowBuilder<Flow>("bankFlow")
                .start(bankFileReadStep())
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
    public Step channelFileReadStep() {
        return stepBuilderFactory.get("channelFileReadStep")
                .<ChannelFile, ChannelFile>chunk(1000)
                .reader(channelFileItemReader())
                .processor(channelFileProcessor)
                .writer(channelFileItemWriter)
                .faultTolerant()
                .skipLimit(5)
                .skip(FlatFileParseException.class)
                .taskExecutor(threadPoolTaskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<ChannelFile> channelFileItemReader() {
        FlatFileItemReader<ChannelFile> reader = new FlatFileItemReader<>();
        File channelFile = new File("D:/train/1002.txt");
        reader.setResource(new FileSystemResource(channelFile));
        return getChannelFileItemReader(reader);
    }


    private FlatFileItemReader<ChannelFile> getChannelFileItemReader(FlatFileItemReader<ChannelFile> reader) {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        HandleErrorRecord tokenizer = new HandleErrorRecord();
        tokenizer.setNames("channelNum", "customerNum", "cardNum", "dealMoney", "dealType", "userName", "commissionType",
                "unionPayFlowNum", "bankFlowNum", "charge1", "charge2", "charge3", "additionalCharge", "remark");
        tokenizer.setDelimiter("|"); // 设置分隔符
        HandleErrorRecord<ChannelFile> lineMapper = new HandleErrorRecord<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet ->
                ChannelFile.builder()
                        .channelNum(fieldSet.readString("channelNum"))
                        .customerNum(fieldSet.readString("customerNum"))
                        .cardNum(fieldSet.readString("cardNum"))
                        .dealMoney(fieldSet.readBigDecimal("dealMoney"))
                        .dealType(fieldSet.readString("dealType"))
                        .userName(fieldSet.readString("userName"))
                        .commissionType(fieldSet.readString("commissionType"))
                        .unionPayFlowNum(fieldSet.readString("unionPayFlowNum"))
                        .bankFlowNum(fieldSet.readString("bankFlowNum"))
                        .charge1(fieldSet.readBigDecimal("charge1"))
                        .charge2(fieldSet.readBigDecimal("charge2"))
                        .charge3(fieldSet.readBigDecimal("charge3"))
                        .additionalCharge(fieldSet.readString("additionalCharge"))
                        .remark(fieldSet.readString("remark"))
                        .build()
        );
        lineMapper.afterPropertiesSet();
        reader.setLineMapper(lineMapper);
        return reader;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<BankFile> bankFileItemReader() {
        FlatFileItemReader<BankFile> reader = new FlatFileItemReader<>();
        File channelFile = new File("D:/train/0002.txt");
        reader.setResource(new FileSystemResource(channelFile));
        return getBankFileItemReader(reader);
    }


    @Bean
    public Step bankFileReadStep() {
        return stepBuilderFactory.get("bankFileReadStep")
                .<BankFile, BankFile>chunk(1000)
                .reader(bankFileItemReader())
//                .processor(channelFileProcessor)
                .writer(bankFileItemWriter)
                .faultTolerant()
                .skipLimit(5)
                .skip(FlatFileParseException.class)
                .taskExecutor(threadPoolTaskExecutor())
                .build();
    }

    private FlatFileItemReader<BankFile> getBankFileItemReader(FlatFileItemReader<BankFile> reader) {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        HandleErrorRecord tokenizer = new HandleErrorRecord();
        tokenizer.setNames("channelNum", "customerNum", "cardNum", "dealMoney", "dealType", "userName", "commissionType",
                "unionPayFlowNum", "bankFlowNum", "charge1", "charge2", "charge3", "additionalCharge", "remark");
        tokenizer.setDelimiter("|"); // 设置分隔符
        HandleErrorRecord<BankFile> lineMapper = new HandleErrorRecord<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet ->
                BankFile.builder()
                        .channelNum(fieldSet.readString("channelNum"))
                        .customerNum(fieldSet.readString("customerNum"))
                        .cardNum(fieldSet.readString("cardNum"))
                        .dealMoney(fieldSet.readBigDecimal("dealMoney"))
                        .dealType(fieldSet.readString("dealType"))
                        .userName(fieldSet.readString("userName"))
                        .commissionType(fieldSet.readString("commissionType"))
                        .unionPayFlowNum(fieldSet.readString("unionPayFlowNum"))
                        .bankFlowNum(fieldSet.readString("bankFlowNum"))
                        .charge1(fieldSet.readBigDecimal("charge1"))
                        .charge2(fieldSet.readBigDecimal("charge2"))
                        .charge3(fieldSet.readBigDecimal("charge3"))
                        .additionalCharge(fieldSet.readString("additionalCharge"))
                        .remark(fieldSet.readString("remark"))
                        .build()
        );
        lineMapper.afterPropertiesSet();
        reader.setLineMapper(lineMapper);
        return reader;
    }


}
