package com.server.http.login;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class FacebookTokenValidatior extends TokenValidator {

	public FacebookTokenValidatior(String token, String userId) {
		super(token, userId);
	}

	@Override
	protected String getUrl() {
		return "https://graph.facebook.com/me";
	}
	
	public boolean validate(JSONObject response) {
		String sub = (String) response.get("sub");
		return sub.equals(getUserId());
	}
	
	@Override
	public boolean validate() throws UnirestException {
		HttpRequest request = Unirest.get(getUrl()).queryString("access_token", this.token);
		HttpResponse<String> response = request.asString();
		return response.getStatus() == 200 ;
	}

}
