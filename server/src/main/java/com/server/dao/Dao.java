package com.server.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class Dao {
	@Inject
	private EntityManager manager;

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}
	
	public void persist(Object obj) {
		manager.getTransaction().begin();
		manager.persist(obj);
		manager.getTransaction().commit();
	}
	
}
