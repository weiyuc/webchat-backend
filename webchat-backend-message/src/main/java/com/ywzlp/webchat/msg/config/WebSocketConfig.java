package com.ywzlp.webchat.msg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

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
		registry.addEndpoint("/webchat").setAllowedOrigins("*").withSockJS()
				.setClientLibraryUrl("//cdn.jsdelivr.net/sockjs/1.1.4/sockjs.min.js");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(getWebSocketInterceptor());
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setMessageSizeLimit(2000000); // default : 64 * 1024
		registration.setSendTimeLimit(20 * 10000); // default : 10 * 10000
		registration.setSendBufferSizeLimit(3 * 512 * 1024); // default : 512 * 1024
	}

}
