package com.ywzlp.webchat.msg.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	private static final String defaultImage = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/wAALCAAyADIBAREA/8QAHAABAAMAAgMAAAAAAAAAAAAAAAEFBwMEAgYJ/8QAMRAAAQMDAQQHCAMAAAAAAAAAAQACAwQFEQYhMUFREhMWImGB0QcVIzVDVZKxkaHB/9oACAEBAAA/APpKiKSMbxhQiIiy/XOs6qouE1BRTugpYSWPdGcOkcN+0cBuwqWw6uuFiqmPE8k9Pn4kEji4OHHGdx8Vs1NUR1dPFPE7pRSND2u5gjIXIiKRsIPisDukElLcquKVpbIyZ4cDz6RXVzjatx0rTyUunLbFKC2RsDcg8M7cf2rRERZf7T6aibc4KiCZhqpAWzxMOSMbnHkeHkqTR9HSVuoKaOtkZHAMvw8gB7htDc+J/S2v/URSBkgc1kOpNcXK51NRBFK6kpA9zBHEcFwBx3nbyvV8JjwVtadU3SykCmqn9UPoyd9n8Hd5LYrLcfe9ppK3odWZ4w8sznB4/pd1FTVWjrLWTvmmt8TpXnLnAubk88Arj7C2H7bH+b/VOwth+2x/m/1QaFsIPy2Pzc71V3FEyCJkcbGxxsAa1jRgAcgvJERERF//2Q==";

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
	public WebChatResponse<?> setRealName(
			@RequestBody @Validated(ValidatorGroups.SetRealName.class) UserRegisterDto user) {
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
	public WebChatResponse<?> setProfilePhoto(
			@RequestBody @Validated(ValidatorGroups.SetProfilePhoto.class) UserRegisterDto user) {
		userService.uploadProfilePhoto(user.getProfilePhoto());
		return WebChatResponse.success();
	}

	@PostMapping("/getNearbyPeoples")
	public WebChatResponse<?> getNearbyPeoples(@RequestBody Point location) {
		List<CoordinateEntity> nearbyPeoples = userService.getNearbyPeoples(location);
		return WebChatResponse.success(nearbyPeoples);
	}

	@PostMapping("/clearLocation")
	public WebChatResponse<?> clearLocation() {
		userService.clearLocation();
		return WebChatResponse.success();
	}

	@GetMapping("/getProfilePhoto/{username}")
	public void getProfilePhoto(@PathVariable("username") String username, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {

		InputStream in = userService.getProfilePhoto(username);
		byte[] bytes = null;
		if (in == null) {
			bytes = Base64.getDecoder().decode(defaultImage);
		} else {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			String base64 = sb.toString().split("base64,")[1];
			bytes = Base64.getDecoder().decode(base64);

			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
		}

		httpServletResponse.setContentType("image/png");
		OutputStream output = httpServletResponse.getOutputStream();
		output.write(bytes);
		output.flush();
		output.close();
	}

	@GetMapping("/getVoice/{id}.wav")
	public HttpEntity<Resource> getVoice(@PathVariable("id") String id) throws IOException {
		GridFsResource resource = userService.getVoice(id);
		InputStream in = resource.getInputStream();
		byte[] bytes = null;
		if (in == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			String base64 = sb.toString().split("base64,")[1];
			bytes = Base64.getDecoder().decode(base64);

			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
		}
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("audio", "x-wav"));
		header.setETag(generateETagHeaderValue(bytes));
		header.setLastModified(resource.lastModified());
		header.setDate(System.currentTimeMillis());
		return new HttpEntity<Resource>(new ByteArrayResource(bytes), header);
	}

	protected String generateETagHeaderValue(byte[] bytes) {
		StringBuilder builder = new StringBuilder("\"0");
		DigestUtils.appendMd5DigestAsHex(bytes, builder);
		builder.append('"');
		return builder.toString();
	}

}
