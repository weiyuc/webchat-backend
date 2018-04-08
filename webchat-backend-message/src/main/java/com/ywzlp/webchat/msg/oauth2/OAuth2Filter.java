package com.ywzlp.webchat.msg.oauth2;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ywzlp.webchat.msg.vo.Response;
import com.ywzlp.webchat.msg.vo.WebChatResponse;

public class OAuth2Filter extends AuthenticatingFilter {

	private static final String NAME = "OAuth2Filter";

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String token = this._getRequestToken(httpRequest);
		if (!StringUtils.isEmpty(token)) {
			return executeLogin(request, response);
		}
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json;charset=utf-8");
		WebChatResponse<?> res = WebChatResponse.error(Response.NOT_AUTHORIZED);
		httpResponse.getWriter().print(JSON.toJSONString(res));
		httpResponse.getWriter().flush();
		return false;
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException authException, ServletRequest request,
			ServletResponse response) {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json;charset=utf-8");
		WebChatResponse<?> res = WebChatResponse.error(Response.TOKEN_EXPIRED);
		try {
			httpResponse.getWriter().print(JSON.toJSONString(res));
			httpResponse.getWriter().flush();
		} catch (IOException ignore) {
		}
		return false;
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String token = httpRequest.getHeader("token");
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		return new OAuth2Token(token);
	}

	private String _getRequestToken(HttpServletRequest httpRequest) {
		return httpRequest.getHeader("token");
	}

	@Override
	protected String getName() {
		return NAME;
	}
}
