package com.server.http;

import javax.inject.Inject;

import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.UnboundLiteral;
import org.jboss.weld.environment.se.WeldContainer;

import redis.clients.jedis.Jedis;

public class Resource {
	@Inject
	private WeldContainer container;

	@Inject
	private Jedis jedis;
	
	public void startResquestScope() {
		container.instance().select(RequestContext.class, UnboundLiteral.INSTANCE)
		.get().activate();
	}
	
	protected Jedis getJedis() {
		return jedis;
	}
	
	protected void tokenRegister(Long userId, String extUserId, String loginService, String token){
		getJedis().set(token + loginService, extUserId);
		getJedis().set(extUserId + loginService, userId.toString());
		
	}
	
	protected String getExternalUserId(String token, String loginService) {
		return getJedis().get(token + loginService);
	}
	
	protected String getUserId(String token, String loginService) {
		String externalUserId = getJedis().get(token + loginService);
		return getJedis().get(externalUserId + loginService);
	}

}
