package com.ywzlp.webchat.msg.repository;

import org.springframework.data.repository.CrudRepository;
import com.ywzlp.webchat.msg.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String> {
	
	int countByUsername(String username);
	
	UserEntity findByUsernameAndPassword(String username, String password);

	UserEntity findByUsername(String friendName);
	
}
