package com.ywzlp.webchat.msg.dto;

public class WebUserToken {
	
	private String username;

	private Integer gender;

	private String whatUp;

	private String realName;

	private String phoneNumber;
	
	private String profilePhoto;

	private String accessToken;

	private Long expiredTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getWhatUp() {
		return whatUp;
	}

	public void setWhatUp(String whatUp) {
		this.whatUp = whatUp;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}
	
}
