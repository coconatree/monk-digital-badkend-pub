package com.monk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityConfig {

    @Bean
    public String createString() {
        return "";
    }

    @Bean
    public Long createLong() {
        return 1L;
    }
}
