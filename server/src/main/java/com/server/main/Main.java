package com.server.main;

import static spark.Spark.secure;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.conf.Configuration;
import com.server.http.HelloWorld;
import com.server.http.RedisTest;

import redis.clients.jedis.Jedis;

public class MainTestCertificado {
	private static final Logger logger = LoggerFactory.getLogger(MainTestCertificado.class);
	
	@Inject
	private Configuration configuration;

	public void main(@Observes ContainerInitialized event) {
		if (configuration.isTLSEnabled()) {
			secure("C:\\Users\\César\\Documents\\certs\\server.jks", "/852789@", null, null);
			logger.info("TLS Enabled");
		}

		Jedis jedis = new Jedis("redis", 6379);
		
		HelloWorld.registerResource();
		RedisTest.registerResource(jedis);
	}
}
