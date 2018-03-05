package com.ywzlp.webchat.msg.config;

import java.util.List;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.util.CollectionUtils;

import com.ywzlp.webchat.msg.dto.WebChatMessage;
import com.ywzlp.webchat.msg.dto.MessageType;
import com.ywzlp.webchat.msg.entity.UserTokenEntity;
import com.ywzlp.webchat.msg.service.UserService;
import com.ywzlp.webchat.msg.util.ConcurrentBidiMap;
import com.ywzlp.webchat.msg.util.ConcurrentHashBidiMap;
import com.ywzlp.webchat.msg.util.SpringUtil;

public class WebSocketInterceptor extends ChannelInterceptorAdapter {
	
	@Autowired
	private UserService userService;
	
	private static final ConcurrentBidiMap<String, String> counter = new ConcurrentHashBidiMap<>();
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		StompCommand command = accessor.getCommand();
		String sessionId = accessor.getSessionId();
		switch (command) {
		case CONNECT:
			String token = this.getToken(message);
			UserTokenEntity userToken = userService.findByAccessToken(token);
			if (userToken == null) {
				throw new IncorrectCredentialsException();
			}
			String username = userToken.getUsername();
			String oldSessionId = counter.getKey(username);
			if (oldSessionId != null) {
				this.pushOut(username);
				counter.removeByValue(username);
			}
			counter.put(sessionId, username);
			break;
		case ABORT:
			counter.remove(sessionId);
			break;
		case DISCONNECT:
			counter.remove(sessionId);
			break;
		default:
			break;
		}
		return message;
	}
	
	private String getToken(Message<?> message) {
		NativeMessageHeaderAccessor header = MessageHeaderAccessor.getAccessor(message, NativeMessageHeaderAccessor.class);
		List<String> token = header.getNativeHeader("token");
		if (CollectionUtils.isEmpty(token)) {
			throw new IncorrectCredentialsException();
		}
		return token.get(0);
	}
	
	public int countOnlineUsers() {
		return counter.size();
	}
	
	public boolean isOnline(String username) {
		return counter.containsValue(username);
	}
	
	/**
	 * 通知用户被挤下线
	 */
	public void pushOut(String username) {
		//发送下线通知
		WebChatMessage msg = new WebChatMessage();
		msg.setMessageType(MessageType.PUSH_OUT);
		SimpMessagingTemplate template = SpringUtil.getBean(SimpMessagingTemplate.class);
		template.convertAndSend("/message/" + username, msg);
	}

}
