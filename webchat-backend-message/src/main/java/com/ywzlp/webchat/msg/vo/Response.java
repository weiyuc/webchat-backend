package com.ywzlp.webchat.msg.vo;

public enum Response {
	
	OK(0, "成功"),
	ARG_NOT_VALID(401, "参数不合法"),
	NOT_AUTHORIZED(403, "未授权"),
	SYS_ERR(500, "系统异常"),
	USERNAME_OR_PASSWD_ERR(4003, "用户名或密码错误"),
	TOKEN_EXPIRED(4004, "登录信息过时"),
	USER_ALREADY_EXIST(4005, "用户已存在");
	
	private int code;
	private String msg;
	
	Response(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
