package com.server.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.model.User;


public class UserDAO extends Dao {
	@Inject
	private EntityManager manager;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

	public User add(User user) {
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		return user;
	}
	
	public User findByUsername(String username) {
		User user = null;
		try {
			Query query = manager.createQuery("from User where username = :username");
			query.setParameter("username", username);
			
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning username: " + username);
		} catch (Exception e) {
			logger.error("Error returning username: " + username, e);
		}
		
		return user;
	}
	
	public User findByUserId(String userId, String loginService) {
		User user = null;
		try {
			Query query = manager.createQuery("from User where userid = :userId and loginservice = :loginservice");
			query.setParameter("userId", userId);
			query.setParameter("loginservice", loginService);
			
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning userId: " + userId);
		} catch (Exception e) {
			logger.error("Error returning userId: " + userId, e);
		}
		
		return user;
	}
}
