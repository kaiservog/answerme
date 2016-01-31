package com.server.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.model.User;
import com.server.model.UserTag;


public class UserTagDAO {
	@Inject
	private EntityManager manager;
	
	private static final Logger logger = LoggerFactory.getLogger(UserTagDAO.class);

	public void add(UserTag userTag) {
		manager.getTransaction().begin();
		try {
			manager.persist(userTag);
			manager.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error inserting userTag: " + userTag.getUser().getUsername());
			manager.getTransaction().rollback();
		}
	}
	
	public UserTag findByUser(User user) {
		UserTag userTag = null;
		try {
			Query query = manager.createQuery("from UserTag where user = :user");
			query.setParameter("user", user.getId());
			
			userTag = (UserTag) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning userTag: " + user.getUsername());
		} catch (Exception e) {
			logger.error("Error returning userTag: " + user.getUsername(), e);
		}
		
		return userTag;
	}
}
