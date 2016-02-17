package com.server.http.login;

import org.json.JSONObject;

public class TesterTokenValidatior extends TokenValidator {

	public TesterTokenValidatior(String token, String userId) {
		super(token, userId);
	}

	@Override
	protected String getUrl() {
		return null;
	}
	
	public boolean validate(JSONObject response) {
		return true;
	}

}
