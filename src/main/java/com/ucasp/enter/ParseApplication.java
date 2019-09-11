package com.ucasp.enter;

import com.cloud.common.annotation.SpringCloudWebApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;

@SpringCloudWebApplication
@EnableBatchProcessing
@MapperScan("com.ucasp.enter.dao")
public class ParseApplication {

	public static void main(String[] args) {

		SpringApplication.run(ParseApplication.class, args);
	}

}
