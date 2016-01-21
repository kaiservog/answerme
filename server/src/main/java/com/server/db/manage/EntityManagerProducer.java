package com.server.db.manage;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProducer {
	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");

	@Produces
	public EntityManager createEntityManager() {
		return factory.createEntityManager();
	}

	public void finalize(@Disposes EntityManager manager) {
		manager.close();
	}

}
