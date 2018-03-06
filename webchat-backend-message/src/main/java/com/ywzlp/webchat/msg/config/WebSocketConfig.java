package com.ywzlp.webchat.msg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
	@Bean
	public WebSocketInterceptor getWebSocketInterceptor() {
		return new WebSocketInterceptor();
	}

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/message");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/webchat").setAllowedOrigins("*").withSockJS().setClientLibraryUrl("//cdn.jsdelivr.net/sockjs/1.1.4/sockjs.min.js");
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(getWebSocketInterceptor());
    }
    
}
