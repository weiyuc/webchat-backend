package com.ywzlp.webchat.msg.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ywzlp.webchat.msg.entity.FriendEntity;

public interface FriendRepository extends CrudRepository<FriendEntity, String> {
	
	List<FriendEntity> findByUsernameAndStatus(String username, Integer status);
	
	List<FriendEntity> findByUsername(String username);
	
	FriendEntity findByUsernameAndFriendName(String username, String friendName);
	
	int deleteByUsernameAndFriendName(String username, String friendName);
	
}
