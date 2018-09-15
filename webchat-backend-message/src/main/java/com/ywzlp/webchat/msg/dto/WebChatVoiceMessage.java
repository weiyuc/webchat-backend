package com.ywzlp.webchat.msg.dto;

public class WebChatVoiceMessage extends WebChatMessage {
	
	private String data;
	
	private long duration;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
	
}
