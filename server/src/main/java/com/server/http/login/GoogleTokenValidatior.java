package com.server.http.login;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class GoogleTokenValidatior extends TokenValidator {

	public GoogleTokenValidatior(String token, String userId) {
		super(token, userId);
	}

	@Override
	protected String getUrl() {
		return "https://www.googleapis.com/oauth2/v3/tokeninfo";
	}
	
	@Override
	public boolean validate() throws UnirestException {
		HttpRequest request = Unirest.get(getUrl()).queryString("id_token", this.token);
		HttpResponse<String> response = request.asString();
		String bodyString = response.getBody();
		JSONObject jsonResponse = new JSONObject(bodyString);
		
		String sub = (String) jsonResponse.get("sub");
		return sub.equals(getUserId());
		
	}

}
