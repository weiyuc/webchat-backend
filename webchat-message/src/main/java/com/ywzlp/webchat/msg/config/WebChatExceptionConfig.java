package com.ywzlp.webchat.msg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebChatExceptionConfig {
	
    @Bean
    public WebChatExceptionHandler globalExceptionHandler() {
        return new WebChatExceptionHandler();
    }
    
}
