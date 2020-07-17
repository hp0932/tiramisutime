package com.custard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CustardApplication {
	
	private static final String PROP = "spring.config.location="
			  + "classpath:/application.properties"
			  + ",classpath:/config.properties";

	public static void main(String[] args) {
		//SpringApplication.run(CustardApplication.class, args);
		new SpringApplicationBuilder(CustardApplication.class)
		.properties(PROP)
		.run(args);
	}

}
