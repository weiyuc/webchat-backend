package com.ywzlp.webchat.msg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mongodb.WriteResult;
import com.ywzlp.webchat.msg.dto.MessageType;
import com.ywzlp.webchat.msg.dto.WebChatMessage;
import com.ywzlp.webchat.msg.dto.WebUserToken;
import com.ywzlp.webchat.msg.entity.UserEntity;
import com.ywzlp.webchat.msg.entity.UserMessageEntity;
import com.ywzlp.webchat.msg.entity.UserTokenEntity;
import com.ywzlp.webchat.msg.repository.UserMessageRepository;
import com.ywzlp.webchat.msg.repository.UserRepository;
import com.ywzlp.webchat.msg.repository.UserTokenRepository;
import com.ywzlp.webchat.msg.util.TokenGenerator;

@Service
public class UserService {
	
	/**
	 * 12 hours
	 */
	private static final Long expiredMills = 1000 * 60 * 60 * 12L;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMessageRepository messageRepository;
	
	@Autowired
	private UserTokenRepository userTokenRepository;
	
	@Autowired
	private MongoOperations mongoOperation;
	
	public UserEntity saveUser(UserEntity user) {
		return userRepository.save(user);
	}
	
	public int updateUser(UserEntity user) {
		Update update = new Update();
		update.set("gender", user.getGender());
		update.set("whatUp", user.getWhatUp());
		update.set("realName", user.getRealName());
		update.set("phoneNumber", user.getPhoneNumber());
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(user.getUsername()));
		return mongoOperation.upsert(query, update, UserEntity.class).getN();
	}
	
	public UserEntity getCurrentUserEntity() {
		return userRepository.findByUsername(getCurrentUsername());
	}
	
	public boolean isExist(String username) {
		return userRepository.countByUsername(username) > 0;
	}
	
	public UserTokenEntity findByAccessToken(String accessToken) {
		UserTokenEntity userToken = userTokenRepository.findByAccessToken(accessToken);
		if (userToken == null) {
			return null;
		}
		if (System.currentTimeMillis() > userToken.getExpiredTime()) {
			userTokenRepository.deleteByAccessToken(accessToken);
			return null;
		}
		return userToken;
	}
	
	public WebUserToken createToken(String username, String password) {
		UserEntity user = userRepository.findByUsernameAndPassword(username, password);
		if (user == null) {
			return null;
		}
		userTokenRepository.deleteByUsername(username);
		UserTokenEntity userToken = new UserTokenEntity();
		userToken.setAccessToken(TokenGenerator.generateToken());
		userToken.setExpiredTime(System.currentTimeMillis() + expiredMills);
		userToken.setUsername(username);
		userTokenRepository.save(userToken);
		
		WebUserToken webUserToken = new WebUserToken();
		webUserToken.setUsername(username);
		webUserToken.setAccessToken(userToken.getAccessToken());
		webUserToken.setExpiredTime(userToken.getExpiredTime());
		webUserToken.setGender(user.getGender());
		webUserToken.setPhoneNumber(user.getPhoneNumber());
		webUserToken.setRealName(user.getRealName());
		webUserToken.setWhatUp(user.getWhatUp());
		return webUserToken;
	}
	
	public List<WebChatMessage> getUnReadMesssages() {
		String to = getCurrentUsername();
		List<UserMessageEntity> messages = messageRepository.findByToAndStatus(to, UserMessageEntity.UN_READ);
		if (CollectionUtils.isEmpty(messages)) {
			return null;
		}
		return messages.stream().map(m -> {
			WebChatMessage message = new WebChatMessage();
			message.setId(m.getMessageId());
			message.setContent(m.getContent());
			message.setFrom(m.getFrom());
			message.setMessageType(MessageType.SMS);
			message.setTo(m.getTo());
			message.setTimestamp(m.getCreateTime());
			return message;
		}).collect(Collectors.toList());
	}
	
	public UserMessageEntity saveMessage(WebChatMessage message) {
		UserMessageEntity userMessage = new UserMessageEntity();
		userMessage.setContent(message.getContent());
		userMessage.setFrom(message.getFrom());
		userMessage.setTo(message.getTo());
		userMessage.setCreateTime(message.getTimestamp());
		userMessage.setStatus(UserMessageEntity.UN_READ);
		return messageRepository.save(userMessage);
	}
	
	public static String getCurrentUsername() {
		UserTokenEntity userToken = (UserTokenEntity) SecurityUtils.getSubject().getPrincipal();
		if (userToken == null) {
			return null;
		}
		return userToken.getUsername();
	}

	public int hasRead(WebChatMessage message) {
		Update update = new Update();
		update.set("status", UserMessageEntity.READED);
		Query query = new Query();
		query.addCriteria(Criteria.where("from").is(message.getFrom()));
		query.addCriteria(Criteria.where("to").is(message.getTo()));
		WriteResult writeResult = mongoOperation.updateMulti(query, update, UserMessageEntity.class);
		return writeResult.getN();
	}

}
