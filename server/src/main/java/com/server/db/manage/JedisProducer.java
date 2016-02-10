package com.server.db.manage;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.server.conf.Configuration;

import redis.clients.jedis.Jedis;

public class JedisProducer {

	@Inject
	private Configuration configuration;
	
	@Produces
	public Jedis create() {
		return new Jedis(configuration.getCacheAddress(), configuration.getCachePort());
	}
}
