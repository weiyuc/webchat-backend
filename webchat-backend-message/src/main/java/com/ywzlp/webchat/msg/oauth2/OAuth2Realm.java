package com.ywzlp.webchat.msg.oauth2;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.Initializable;
import org.springframework.beans.factory.annotation.Autowired;

import com.ywzlp.webchat.msg.entity.UserTokenEntity;
import com.ywzlp.webchat.msg.service.UserService;

public class OAuth2Realm extends AuthenticatingRealm implements Initializable {

	@Autowired
	private UserService userService;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String accessToken = (String) token.getPrincipal();
		UserTokenEntity userToken = userService.findByAccessToken(accessToken);
		if (userToken == null) {
			throw new IncorrectCredentialsException();
		}
		return new SimpleAuthenticationInfo(userToken, accessToken, getName());
	}
	
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof OAuth2Token;
	}
	
}
