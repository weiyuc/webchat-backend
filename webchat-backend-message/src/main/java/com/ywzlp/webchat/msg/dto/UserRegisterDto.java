package com.ywzlp.webchat.msg.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import com.ywzlp.webchat.msg.validator.ValidatorGroups;

public class UserRegisterDto extends AbstractDto {
	
	@NotBlank(groups = {ValidatorGroups.Register.class}, message = "username can not be null")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$", groups = {ValidatorGroups.Register.class}, message = "username invalid")
	private String username;
	
	@NotBlank(groups = {ValidatorGroups.Register.class}, message = "password can not be null")
	@Pattern(regexp = "^[a-zA-Z]\\w{5,17}$", groups = {ValidatorGroups.Register.class}, message = "password invalid")
	private String password;
	
	private Integer gender;
	
	@Size(groups = {ValidatorGroups.SetRealName.class}, message = "realName was too large", min = 0, max = 20)
	private String realName;
	
	@Size(groups = {ValidatorGroups.SetWhatUp.class}, message = "whatUp was too large", min = 0, max = 150)
	private String whatUp;
	
	@NotNull(groups = {ValidatorGroups.SetProfilePhoto.class}, message = "profilePhoto can not be null")
	@Size(groups = {ValidatorGroups.SetProfilePhoto.class}, message = "profilePhoto was too large", min = 0, max = 200 * 1024)
	private String profilePhoto;
	
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getWhatUp() {
		return whatUp;
	}

	public void setWhatUp(String whatUp) {
		this.whatUp = whatUp;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	
}
