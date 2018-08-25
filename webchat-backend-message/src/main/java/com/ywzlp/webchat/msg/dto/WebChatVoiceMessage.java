package com.ywzlp.webchat.msg.dto;

import java.util.Map;

public class WebChatVoiceMessage extends WebChatMessage {
	
	private Map<String, Long> data;
	
	private long duration;
	
	public Map<String, Long> getData() {
		return data;
	}

	public void setData(Map<String, Long> data) {
		this.data = data;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
	
}
