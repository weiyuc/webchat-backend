package com.ywzlp.webchat.msg.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user_message")
public class UserMessageEntity {
	
	/**
	 * Un read status
	 */
	@Transient
	public static final Integer UN_READ = 0;
	
	/**
	 * Readed status
	 */
	@Transient
	public static final Integer READED = 1;
	
	@Id
	private String messageId;
	
	@Field("from")
	private String from;
	
	@Field("to")
	private String to;
	
	@Field("content")
	private String content;
	
	@Field("createTime")
	private Long createTime;
	
	@Field("status")
	private Integer status;
	
	@Field("duration")
	private Long duration;
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
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

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
}
