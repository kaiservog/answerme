package com.server.main;

import static spark.Spark.secure;

import java.io.IOException;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.conf.Configuration;
import com.server.http.HelloWorld;
import com.server.http.QuestionResource;
import com.server.http.RedisTest;
import com.server.http.SparkFilter;
import com.server.http.UserResource;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	@Inject
	private UserResource userResource;
	@Inject
	private RedisTest redistTest;
	@Inject
	private QuestionResource questionResource;
	@Inject
	private Configuration configuration;

	public static void main(String[] args) throws IOException {
		WeldContainer container = new Weld().initialize();
		container.instance().select(Main.class).get().execute();
	}

	public void execute() throws IOException {
		configuration.load();
		
		if (configuration.isSecureEnabled()) {
			secure(configuration.getKeyStorePath(), configuration.getKeyStorePass(), null, null);
			logger.info("Secure enabled");
		}

		registerResources();

		logger.info("Main executed");
	}

	private void registerResources() {
		HelloWorld.registerResource();
		SparkFilter.registerResource();
		userResource.registerResource();
		redistTest.registerResource();
		questionResource.registerResource();
	}
}
