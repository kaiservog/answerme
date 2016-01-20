package com.server.main;

import static spark.Spark.get;
import static spark.Spark.secure;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import com.server.conf.Configuration;

public class MainTestCertificado {
	@Inject
	private Configuration configuration;

	public void main(@Observes ContainerInitialized event) {
		if (configuration.isTLSEnabled()) {
			secure("C:\\Users\\César\\Documents\\certs\\server.jks", "/852789@", null, null);
		}
		
		get("/hello", (req, res) -> "Hello World");
		System.out.println("teste");

	}
}
