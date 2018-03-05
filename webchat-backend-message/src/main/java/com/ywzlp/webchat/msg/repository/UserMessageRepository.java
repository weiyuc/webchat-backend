package com.ywzlp.webchat.msg.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ywzlp.webchat.msg.entity.UserMessageEntity;

public interface UserMessageRepository extends CrudRepository<UserMessageEntity, String> {
	
	List<UserMessageEntity> findByToAndStatus(String to, Integer status);
	
}
