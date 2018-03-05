package com.ywzlp.webchat.msg.vo;

public class WebChatResponse<T> {
	
	private int responseCode;
	
	private String responseMsg;
	
	private T responseData;

	public int getResponseCode() {
		return responseCode;
	}

	public WebChatResponse<T> setResponseCode(int responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public WebChatResponse<T> setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
		return this;
	}

	public T getResponseData() {
		return responseData;
	}

	public WebChatResponse<T> setResponseData(T responseData) {
		this.responseData = responseData;
		return this;
	}
	
	public static WebChatResponse<?> success() {
		return new WebChatResponse<>();
	}
	
	public static <T> WebChatResponse<T> success(T t) {
		return new WebChatResponse<T>().setResponseData(t);
	}
	
	public static WebChatResponse<?> error(Response res) {
		return new WebChatResponse<>().setResponseCode(res.getCode()).setResponseMsg(res.getMsg());
	}
	
}
