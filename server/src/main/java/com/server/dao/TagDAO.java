package com.server.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.model.Tag;


public class TagDAO {
	@Inject
	private EntityManager manager;
	
	private static final Logger logger = LoggerFactory.getLogger(TagDAO.class);

	public void add(Tag tag) {
		manager.getTransaction().begin();
		try {
			manager.persist(tag);
			manager.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error inserting tagname: " + tag.getName());
			manager.getTransaction().rollback();
		}
	}
	
	public Tag findByName(String name) {
		Tag tag = null;
		try {
			Query query = manager.createQuery("from Tag where name = :name");
			query.setParameter("name", name);
			
			tag = (Tag) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning tag: " + name);
		} catch (Exception e) {
			logger.error("Error returning tag: " + name, e);
		}
		
		return tag;
	}
}
