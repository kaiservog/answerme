package com.server.http;

import java.util.Set;

import javax.inject.Inject;

import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.UnboundLiteral;
import org.jboss.weld.environment.se.WeldContainer;
import org.json.JSONObject;

import com.server.conf.RedisClient;
import com.server.controller.UserController;
import com.server.model.Question;
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
	
	public void registerUserTopics(final User user) {
		user.getTopics().forEach((topic) -> { 
			getJedisClient().getJedis().sadd(user.getId() + ".topics", topic.getName());
		});
	}
	
	public Set<String> getUserTopics(final User user) {
		return getJedisClient().getJedis().smembers(user.getId() + ".topics");
	}
	
	public void registerUserAnsweredQuestion(long userQueristId, long questionId) {
		getJedisClient().getJedis().lpush(userQueristId + ".answered_question", String.valueOf(questionId));
	}
	
	public Long getUserAnsweredQuestion(long userQueristId) {
		String r = getJedisClient().getJedis().rpop(userQueristId + ".answered_question");
		if(r == null) return null;
		return Long.valueOf(r);
	}
	
	public User getRequestedUser(JSONObject jsonRequest) {
		String token = jsonRequest.getString("token");
		String loginService = jsonRequest.getString("loginService");
		
		return userController.getById(getUserId(token, loginService));
	}

	public UserController getUserController() {
		return userController;
	}
	
	
}
