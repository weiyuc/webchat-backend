package com.ywzlp.webchat.msg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ywzlp.webchat.msg.dto.UserLoginDto;
import com.ywzlp.webchat.msg.dto.UserRegisterDto;
import com.ywzlp.webchat.msg.dto.WebUserToken;
import com.ywzlp.webchat.msg.entity.CoordinateEntity;
import com.ywzlp.webchat.msg.entity.UserEntity;
import com.ywzlp.webchat.msg.service.UserService;
import com.ywzlp.webchat.msg.validator.ValidatorGroups;
import com.ywzlp.webchat.msg.vo.Response;
import com.ywzlp.webchat.msg.vo.WebChatResponse;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public WebChatResponse<?> register(@RequestBody @Validated(ValidatorGroups.Register.class) UserRegisterDto user) {
		boolean isExist = userService.isExist(user.getUsername());
		if (isExist) {
			return WebChatResponse.error(Response.USER_ALREADY_EXIST);
		}
		userService.saveUser(user.toEntity(UserEntity.class));
		return WebChatResponse.success();
	}
	
	@PostMapping("/login")
	public WebChatResponse<?> login(@RequestBody @Validated(ValidatorGroups.Login.class) UserLoginDto user) {
		WebUserToken userToken = userService.createToken(user.getUsername(), user.getPassword());
		if (userToken == null) {
			return WebChatResponse.error(Response.USERNAME_OR_PASSWD_ERR);
		}
		return WebChatResponse.success(userToken);
	}
	
	@PostMapping("/setRealName")
	public WebChatResponse<?> setRealName(@RequestBody @Validated(ValidatorGroups.SetRealName.class) UserRegisterDto user) {
		UserEntity userEntity = userService.getCurrentUserEntity();
		if (userEntity == null) {
			return WebChatResponse.error(Response.USER_ALREADY_EXIST);
		}
		userEntity.setRealName(user.getRealName());
		userService.updateUser(userEntity);
		return WebChatResponse.success();
	}
	
	@PostMapping("/setGender")
	public WebChatResponse<?> setGender(@RequestBody UserRegisterDto user) {
		UserEntity userEntity = userService.getCurrentUserEntity();
		if (userEntity == null) {
			return WebChatResponse.error(Response.USER_ALREADY_EXIST);
		}
		userEntity.setGender(user.getGender());
		userService.updateUser(userEntity);
		return WebChatResponse.success();
	}
	
	@PostMapping("/setWhatUp")
	public WebChatResponse<?> setWhatUp(@RequestBody @Validated(ValidatorGroups.SetWhatUp.class) UserRegisterDto user) {
		UserEntity userEntity = userService.getCurrentUserEntity();
		if (userEntity == null) {
			return WebChatResponse.error(Response.USER_ALREADY_EXIST);
		}
		userEntity.setWhatUp(user.getWhatUp());
		userService.updateUser(userEntity);
		return WebChatResponse.success();
	}
	
	@PostMapping("/setProfilePhoto")
	public WebChatResponse<?> setProfilePhoto(@RequestBody @Validated(ValidatorGroups.SetProfilePhoto.class) UserRegisterDto user) {
		UserEntity userEntity = userService.getCurrentUserEntity();
		if (userEntity == null) {
			return WebChatResponse.error(Response.USER_ALREADY_EXIST);
		}
		userEntity.setProfilePhoto(user.getProfilePhoto());
		userService.updateUser(userEntity);
		return WebChatResponse.success();
	}
	
	@PostMapping("/getNearbyPeoples")
	public WebChatResponse<?> getNearbyPeoples(@RequestBody Point location) {
		List<CoordinateEntity> nearbyPeoples = userService.getNearbyPeoples(location);
		return WebChatResponse.success(nearbyPeoples);
	}
	
}
