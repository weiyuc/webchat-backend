package com.ywzlp.webchat.msg.dto;


import javax.validation.constraints.NotBlank;

import com.ywzlp.webchat.msg.validator.ValidatorGroups;

public class UserLoginDto {
	
	@NotBlank(groups = {ValidatorGroups.Login.class}, message = "username can not be null")
	private String username;
	
	@NotBlank(groups = {ValidatorGroups.Login.class}, message = "password can not be null")
	private String password;

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
	
}
