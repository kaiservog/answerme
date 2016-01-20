package com.server.http;

import static spark.Spark.get;

import redis.clients.jedis.Jedis;

public class RedisTest {
	public static void registerResource(Jedis jedis) {
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
