package com.ywzlp.webchat.msg.repository;

import java.util.List;

import org.springframework.data.geo.Polygon;
import org.springframework.data.repository.CrudRepository;

import com.ywzlp.webchat.msg.entity.CoordinateEntity;

public interface CoordinateRepository extends CrudRepository<CoordinateEntity, String> {
	
	List<CoordinateEntity> findByLocationWithin(Polygon polygon);

}
