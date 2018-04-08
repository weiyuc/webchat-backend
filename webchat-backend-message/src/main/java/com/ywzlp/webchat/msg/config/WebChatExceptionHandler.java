package com.ywzlp.webchat.msg.config;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ywzlp.webchat.msg.vo.Response;
import com.ywzlp.webchat.msg.vo.WebChatResponse;

@RestControllerAdvice
public class WebChatExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(WebChatExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public WebChatResponse<?> argumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
		
		String msg = e.getBindingResult()
				.getAllErrors()
				.stream()
				.collect(Collectors.toMap(ObjectError::getObjectName, ObjectError::getDefaultMessage, (s, a) -> s + " and "+ a))
				.toString();
		
		log.warn(msg);
		
		return WebChatResponse.error(Response.ARG_NOT_VALID);
	}
	
	@ExceptionHandler(Exception.class)
	public WebChatResponse<?> unCatchExceptionHandler(Exception e) {
		log.error("Un catch exception handler: ", e);
		return WebChatResponse.error(Response.SYS_ERR);
	}

}
