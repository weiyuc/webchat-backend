package com.ywzlp.webchat.msg.vo;

public enum FriendStatus {
	/**
	 * 发出好友请求
	 */
	REQUEST(0),

	/**
	 * 收到好友请求
	 */
	RECEIVE(1),

	/**
	 * 同意
	 */
	ACCEPT(2),
	
	/**
	 * 拒绝
	 */
	REFUSE(3),
	
	/**
	 * 删除
	 */
	DELETE(4),

	/**
	 * 黑名单
	 */
	BLACK_LIST(-1);

	private Integer status;

	FriendStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public static FriendStatus valueOf(int value) {
		switch (value) {
		case 0:
			return REQUEST;
		case 1:
			return RECEIVE;
		case 2:
			return ACCEPT;
		case 3:
			return REFUSE;
		case 4:
			return DELETE;
		case -1:
			return BLACK_LIST;
		default:
			throw new IllegalArgumentException("Unknow FriendStatus of value: " + value);
		}
	}

}
