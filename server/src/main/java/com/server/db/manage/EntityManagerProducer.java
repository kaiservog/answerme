package com.server.db.manage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.conf.Configuration;

public class EntityManagerProducer {
	private static final Logger logger = LoggerFactory.getLogger(EntityManagerProducer.class);
	private Properties properties;
	private EntityManagerFactory factory;
	
	@Inject
	private Configuration configuration;
	
	@Produces
	@RequestScoped
	public EntityManager createEntityManager() {
		if (properties == null) loadProperties();
		if (factory == null) factory = Persistence.createEntityManagerFactory("default", properties);
		return factory.createEntityManager();
	}

	private void loadProperties() {
		try {
			properties = new Properties();
			Path file = Paths.get(configuration.getStoredConfPath());
			final InputStream jpaFileInput = Files.newInputStream(file);
			properties.load(jpaFileInput);
		} catch (Exception e) {
			logger.error("Error creating entityManager", e);
		}
	}

	public void finalize(@Disposes EntityManager manager) {
		manager.close();
	}
}
