package com.ywzlp.webchat.msg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ywzlp.webchat.msg.dto.UserLoginDto;
import com.ywzlp.webchat.msg.dto.UserRegisterDto;
import com.ywzlp.webchat.msg.entity.UserEntity;
import com.ywzlp.webchat.msg.entity.UserTokenEntity;
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
		userService.insertUser(user.toEntity(UserEntity.class));
		return WebChatResponse.success();
	}
	
	@PostMapping("/login")
	public WebChatResponse<?> login(@RequestBody @Validated(ValidatorGroups.Login.class) UserLoginDto user) {
		UserTokenEntity userToken = userService.createToken(user.getUsername(), user.getPassword());
		if (userToken == null) {
			return WebChatResponse.error(Response.USERNAME_OR_PASSWD_ERR);
		}
		return WebChatResponse.success(userToken);
	}
	
}
