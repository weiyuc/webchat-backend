package com.ywzlp.webchat.msg.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ywzlp.webchat.msg.dto.MessageType;
import com.ywzlp.webchat.msg.dto.WebChatMessage;
import com.ywzlp.webchat.msg.dto.WebChatVoiceMessage;
import com.ywzlp.webchat.msg.entity.UserMessageEntity;
import com.ywzlp.webchat.msg.service.UserService;
import com.ywzlp.webchat.msg.validator.ValidatorGroups;
import com.ywzlp.webchat.msg.vo.WebChatResponse;

@Controller
public class NotifyController {
	
	private static final Logger logger = LoggerFactory.getLogger(NotifyController.class);
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private UserService userService;
	
	@MessageMapping("/notify")
    public void notify(@RequestBody @Validated(ValidatorGroups.SendMessage.class)  WebChatMessage message) {
		if (MessageType.HAS_READ.equals(message.getMessageType())) {
			userService.hasRead(message);
			return;
		}
		logger.info("On message: {}", JSON.toJSONString(message));
		UserMessageEntity userMessage = userService.saveMessage(message);
		message.setId(userMessage.getMessageId());
        template.convertAndSend("/message/" + message.getTo(), message);
    }
	
	@MessageMapping("/voiceNotify")
    public void notify(@RequestBody @Validated(ValidatorGroups.SendMessage.class)  WebChatVoiceMessage message) {
		if (MessageType.HAS_READ.equals(message.getMessageType())) {
			userService.hasRead(message);
			return;
		}
		logger.info("On voice message");
		UserMessageEntity userMessage = userService.saveVoiceMessage(message);
		message.setId(userMessage.getMessageId());
		message.setData(null);
        template.convertAndSend("/message/" + message.getTo(), message);
    }
	
	@ResponseBody
	@GetMapping("/message/getUnReadMessages")
	public WebChatResponse<?> getUnReadMessages() {
		List<WebChatMessage> messages = userService.getUnReadMesssages();
		return WebChatResponse.success(messages);
	}
	
}
