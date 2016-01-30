package com.server.main;

import static spark.Spark.secure;

import java.io.IOException;

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
import com.server.http.SparkFilter;
import com.server.http.UserResource;

import redis.clients.jedis.Jedis;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	@Inject
	private UserController userController;

	public static void main(String[] args) {
		StartMain.main(args);
	}

	public void main(@Observes ContainerInitialized event) throws IOException {
		Configuration.load();
		
		if (Configuration.isSecureEnabled()) {
			secure(Configuration.getKeyStorePath(), Configuration.getKeyStorePass(), null, null);
			logger.info("Secure enabled");
		}

		Jedis jedis = new Jedis(Configuration.getCacheAddress(), Configuration.getCachePort());

		HelloWorld.registerResource();
		SparkFilter.registerResource();
		RedisTest.registerResource(jedis);
		UserResource.registerResource(userController);
		logger.info("Main executed");
	}
}
