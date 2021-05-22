package com.free.common.config;

import com.free.common.interceptor.MlogPathInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MlogWebConfig implements WebMvcConfigurer {

    @Bean
    MlogPathInterceptor getMlogPathInterceptor() {
        return new MlogPathInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMlogPathInterceptor()).addPathPatterns("/**");
    }
}