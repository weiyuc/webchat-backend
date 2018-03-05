package com.ywzlp.webchat.msg.dto;

import org.springframework.beans.BeanUtils;

public abstract class AbstractDto {
	
	public <T> T toEntity(Class<T> clazz) {
		T t = null;
		try {
			t = clazz.getConstructor().newInstance();
			BeanUtils.copyProperties(this, t);
		} catch (Exception e) {
			throw new RuntimeException("DTO cast to Entity Exception: ", e);
		}
		return t;
	}
	
}
