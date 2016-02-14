package com.server.http.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.exceptions.UnirestException;

public abstract class TokenValidator {

	private static final Logger logger = LoggerFactory.getLogger(TokenValidator.class);
	protected String token;
	protected String userId;

	public boolean validate() throws UnirestException {
		logger.warn("user test login " + userId);
		return true;
	}

	public TokenValidator(String token, String userId) {
		this.token = token;
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	protected abstract String getUrl();

}
