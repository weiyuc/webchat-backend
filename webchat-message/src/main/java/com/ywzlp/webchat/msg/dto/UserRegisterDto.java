package com.ywzlp.webchat.msg.dto;

import org.hibernate.validator.constraints.NotBlank;

import com.ywzlp.webchat.msg.validator.ValidatorGroups;

public class UserRegisterDto extends AbstractDto {
	
	@NotBlank(groups = {ValidatorGroups.Register.class}, message = "username can not be null")
	private String username;
	
	@NotBlank(groups = {ValidatorGroups.Register.class}, message = "password can not be null")
	private String password;
	
	private Integer gender;
	
	private String phoneNumber;

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

}
