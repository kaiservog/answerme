package com.server.http;

import org.json.JSONObject;

public class GoogleTokenValidatior extends TokenValidator {

	public GoogleTokenValidatior(String token, String userId) {
		super(token, userId);
	}

	@Override
	protected String getUrl() {
		return "https://www.googleapis.com/oauth2/v3/tokeninfo";
	}
	
	public boolean validate(JSONObject response) {
		
		String sub = (String) response.get("sub");
		return sub.equals(getUserId());
		
	}

}
