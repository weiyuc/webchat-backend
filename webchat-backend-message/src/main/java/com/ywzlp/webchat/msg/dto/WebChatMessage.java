package com.ywzlp.webchat.msg.dto;

import javax.validation.constraints.NotBlank;

import com.ywzlp.webchat.msg.validator.ValidatorGroups;

public class WebChatMessage {
	
	private String id;
	
	@NotBlank(groups = {ValidatorGroups.SendMessage.class}, message = "from can not be null")
	private String from;
	
	@NotBlank(groups = {ValidatorGroups.SendMessage.class}, message = "to can not be null")
	private String to;
	
	private String content;
	
	private long timestamp;
	
	private MessageType messageType;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
}
