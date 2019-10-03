package com.crud.a4.boot.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com" })
@EnableJpaRepositories(basePackages = { "com" })
@EntityScan(basePackages = { "com" })
@ComponentScan({ "com" })
public class CrudBootApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CrudBootApplication.class, args);
	}

	/*War Deployment Procedure*/
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CrudBootApplication.class);
	}

}