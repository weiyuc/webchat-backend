package com.ywzlp.webchat.msg.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user_info")
public class UserEntity {
	
	@Id
	private String userId;
	
	@Field("username")
	private String username;
	
	@Field("password")
	private String password;
	
	@Field("gender")
	private Integer gender;
	
	@Field("whatIsUp")
	private Integer whatIsUp;
	
	@Field("realName")
	private Integer realName;
	
	@Field("phone_number")
	private String phoneNumber;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getWhatIsUp() {
		return whatIsUp;
	}

	public void setWhatIsUp(Integer whatIsUp) {
		this.whatIsUp = whatIsUp;
	}

	public Integer getRealName() {
		return realName;
	}

	public void setRealName(Integer realName) {
		this.realName = realName;
	}
	
}
