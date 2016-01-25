package com.server.main;

import static spark.Spark.secure;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.conf.Configuration;
import com.server.controller.UserController;
import com.server.http.HelloWorld;
import com.server.http.RedisTest;
import com.server.http.UserResource;

import redis.clients.jedis.Jedis;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	@Inject
	private Configuration configuration;
	@Inject
	private UserController userController;

	public static void main(String[] args) {
		StartMain.main(args);
	}

	public void main(@Observes ContainerInitialized event) {
		if (configuration.isTLSEnabled()) {
			secure("/apl/server.jks", "/852789@", null, null);
			logger.info("TLS Enabled");
			// TODO configurar saida de log
		}

		Jedis jedis = new Jedis("192.168.0.7", 6379);

		HelloWorld.registerResource();
		RedisTest.registerResource(jedis);
		UserResource.registerResource(userController);
		logger.info("Main executed");
	}
}
