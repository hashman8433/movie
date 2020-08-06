package com.free;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class VideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoApplication.class, args);
	}

}
