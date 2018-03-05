package com.ywzlp.webchat.msg.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

public class OAuth2Token implements AuthenticationToken {
	
	private static final long serialVersionUID = 809071119009498305L;

	private String token;
	
	public OAuth2Token(String token) {
		this.token = token;
	}
	
	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
	
}
