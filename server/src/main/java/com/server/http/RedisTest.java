package com.server.http;

import static spark.Spark.get;

import javax.inject.Inject;

import com.server.conf.Configuration;

import redis.clients.jedis.Jedis;

public class RedisTest {
	
	@Inject
	private Configuration configuration;
	private Jedis jedis;
	
	public void registerResource() {
		jedis = new Jedis(configuration.getCacheAddress(), configuration.getCachePort());
		
		get("/add/*", (request, response) -> {
			try {
				jedis.set(request.splat()[0], request.splat()[0] + "-testevalor");
				return "ok";
			} catch (Exception e) {
				System.out.println(e);
			}
			return "error";
		});
		
		get("/get/:key", (request, response) -> {
			try {
				return jedis.get(request.params(":key"));
				
			} catch (Exception e) {
				System.out.println(e);
			}
			return "error";
		});
	}
}
