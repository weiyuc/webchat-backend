package com.ywzlp.webchat.msg.repository;

import org.springframework.data.repository.CrudRepository;

import com.ywzlp.webchat.msg.entity.UserTokenEntity;

public interface UserTokenRepository extends CrudRepository<UserTokenEntity, String> {
	
	UserTokenEntity findByAccessToken(String userToken);
	
	int deleteByAccessToken(String userToken);

	int deleteByUsername(String username);

	UserTokenEntity findByUsername(String username);

	UserTokenEntity findByUsernameAndAccessTokenNot(String username, String accessToken);
	
}
