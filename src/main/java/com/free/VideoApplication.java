package com.free;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.free.video.dao")
public class VideoApplication {

	public static ConfigurableApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(VideoApplication.class, args);
	}

	public static <T> T  getBean(Class<T> clazz) {
		return applicationContext != null?applicationContext.getBean(clazz):null;
	}
}

