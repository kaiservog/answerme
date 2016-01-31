package com.server.http;

import javax.inject.Singleton;

@Singleton
public class TokenValidatorFactory {
	public TokenValidator get(String key, String token, String userId) {
		if("gp".equals(key)) return new GoogleTokenValidatior(token, userId);
		else return null;
	}
}
