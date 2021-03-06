package com.free;

import com.free.common.utils.SystemConfigUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching //启用缓存
@EnableJpaRepositories(basePackages = "com.free.video.dao")
public class VideoApplication {

	public static ConfigurableApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(VideoApplication.class, args);
		//将run方法的返回值赋值给工具类中的静态变量
		SystemConfigUtils.applicationContext = applicationContext;
	}

	public static <T> T  getBean(Class<T> clazz) {
		return applicationContext != null?applicationContext.getBean(clazz):null;
	}
}

