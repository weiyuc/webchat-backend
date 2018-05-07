package com.ywzlp.webchat.msg.repository;

import org.springframework.data.repository.CrudRepository;

import com.ywzlp.webchat.msg.entity.CoordinateEntity;

public interface CoordinateRepository extends CrudRepository<CoordinateEntity, String> {
	
	CoordinateEntity findByUsername(String username);

	int deleteByUsername(String username);

}
