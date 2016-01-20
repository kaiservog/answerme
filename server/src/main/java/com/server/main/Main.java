package com.server.main;

import static spark.Spark.secure;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import com.server.conf.Configuration;
import com.server.http.HelloWorld;
import com.server.http.RedisTest;

import redis.clients.jedis.Jedis;

public class Main {
	@Inject
	private Configuration configuration;

	public void main(@Observes ContainerInitialized event) {
		if (configuration.isTLSEnabled()) {
			secure("C:\\Users\\C�sar\\Documents\\certs\\server.jks", "/852789@", null, null);
		}
		
		Jedis jedis = new Jedis("redis", 6379);
		
		HelloWorld.registerResource();
		RedisTest.registerResource(jedis);
	}
}
