package com.server.http.login;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public abstract class TokenValidator {

	private static final Logger logger = LoggerFactory.getLogger(TokenValidator.class);
	protected String token;
	protected String userId;

	public boolean validate() throws UnirestException {
		logger.info("Validating token on url: " + getUrl());
		String bodyString = accessTokenService();
		logger.info("response for token is: " + bodyString);
		JSONObject body = new JSONObject(bodyString);
		return this.validate(body);
	}

	private String accessTokenService() throws UnirestException {
		HttpRequest request = Unirest.get(getUrl()).queryString("id_token", this.token);

		HttpResponse<String> response = request.asString();

		String bodyString = response.getBody();
		return bodyString;
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

	protected abstract boolean validate(JSONObject response);

	protected abstract String getUrl();

}
