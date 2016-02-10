package com.server.http;

import javax.inject.Inject;

import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.UnboundLiteral;
import org.jboss.weld.environment.se.WeldContainer;

import com.server.conf.RedisClient;

import redis.clients.jedis.Jedis;

public class Resource {
	@Inject
	private WeldContainer container;

	@Inject
	private RedisClient jedisClient;
	
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
}
