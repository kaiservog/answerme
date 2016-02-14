package com.server.http;

import javax.inject.Inject;

import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.UnboundLiteral;
import org.jboss.weld.environment.se.WeldContainer;
import org.json.JSONObject;

import com.server.conf.RedisClient;
import com.server.controller.UserController;
import com.server.model.User;

public class Resource {
	@Inject
	private WeldContainer container;

	@Inject
	private RedisClient jedisClient;
	
	@Inject
	private UserController userController;
	
	public void startResquestScope() {
		container.instance().select(RequestContext.class, UnboundLiteral.INSTANCE)
			.get().activate();
	}
	
	
	protected void tokenRegister(Long userId, String extUserId, String loginService, String token){
		jedisClient.getJedis().set(token + loginService, extUserId);
		jedisClient.getJedis().set(extUserId + loginService, userId.toString());
	}
	
	protected String getExternalUserId(String token, String loginService) {
		return jedisClient.getJedis().get(token + loginService);
	}
	
	protected String getUserId(String token, String loginService) {
		String externalUserId = jedisClient.getJedis().get(token + loginService);
		return jedisClient.getJedis().get(externalUserId + loginService);
	}

	public RedisClient getJedisClient() {
		return jedisClient;
	}
	
	public User getResquestedUser(JSONObject jsonRequest) {
		String token = jsonRequest.getString("token");
		String loginService = jsonRequest.getString("loginService");
		
		return userController.getById(getUserId(token, loginService));
	}

	public UserController getUserController() {
		return userController;
	}
	
	
}
