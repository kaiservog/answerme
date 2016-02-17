package com.server.http.login;

import javax.inject.Singleton;

@Singleton
public class TokenValidatorFactory {
	public TokenValidator get(String key, String token, String userId) {
		if("gp".equals(key)) return new GoogleTokenValidatior(token, userId);
		else if("fb".equals(key)) return new FacebookTokenValidatior(token, userId);
		else if("tt".equals(key)) return new TesterTokenValidatior(token, userId);
		else return null;
	}
}
