package com.server.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class Dao<T> {
	@Inject private EntityManager manager;
	private ThreadLocal<EntityManager> threadLocal;

	public Dao() {
		threadLocal = new ThreadLocal<>();
		threadLocal.set(manager);
	}
	
	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	public void persist(T obj) {
		manager.getTransaction().begin();
		manager.persist(obj);
		manager.getTransaction().commit();
	}

}
