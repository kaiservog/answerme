package com.server.main;

import static spark.Spark.get;
import static spark.Spark.secure;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.conf.Configuration;

public class MainTestCertificado {
	private static final Logger logger = LoggerFactory.getLogger(MainTestCertificado.class);
	
	@Inject
	private Configuration configuration;

	public void main(@Observes ContainerInitialized event) {
		if (configuration.isTLSEnabled()) {
			secure("C:\\Users\\César\\Documents\\certs\\server.jks", "/852789@", null, null);
			logger.info("TLS Enabled");
		}

		get("/hello", (req, res) -> "Hello World");
		System.out.println("teste");

	}
}
