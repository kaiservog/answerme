package com.server.conf;

import javax.inject.Singleton;

import redis.clients.jedis.Jedis;

@Singleton
public class RedisClient {

	private Jedis jedis;
	
	public void load(String host, int port) {
		jedis = new Jedis(host, port);
	}
	
	public Jedis getJedis() {
		return jedis;
	}
}
