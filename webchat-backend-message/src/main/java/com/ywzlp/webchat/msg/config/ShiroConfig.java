package com.ywzlp.webchat.msg.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ywzlp.webchat.msg.oauth2.OAuth2Filter;
import com.ywzlp.webchat.msg.oauth2.OAuth2Realm;

@Configuration
public class ShiroConfig {
	
	@Bean("oAuth2Realm")
	public OAuth2Realm getOauth2Realm() {
		return new OAuth2Realm();
	}

	@Bean("sessionManager")
	public SessionManager getSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdCookieEnabled(false);
		return sessionManager;
	}

	@Bean("securityManager")
	public SecurityManager getSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(getOauth2Realm());
		securityManager.setSessionManager(getSessionManager());

		return securityManager;
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shirFilter() {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(getSecurityManager());

		Map<String, String> filterMap = new LinkedHashMap<>();
		filterMap.put("/user/login", "anon");
		filterMap.put("/user/register", "anon");
		filterMap.put("/user/getProfilePhoto/**", "anon");
		filterMap.put("/user/getVoice/**", "anon");
		
		filterMap.put("/webchat/**", "anon");
		filterMap.put("/**", "OAuth2");
		
		Map<String, Filter> filters = new HashMap<>();
		filters.put("OAuth2", new OAuth2Filter());
		shiroFilter.setFilters(filters);
		shiroFilter.setFilterChainDefinitionMap(filterMap);
		
		return shiroFilter;
	}

	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(getSecurityManager());
		return advisor;
	}

}
