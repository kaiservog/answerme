package com.server.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.db.TransactionRequired;
import com.server.model.User;

public class UserDAO extends Dao<User> {
	private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

	@TransactionRequired
	public User add(User user) {
		try {
			getManager().persist(user);
		} catch (Exception e) {
			logger.error("Error inserting userid: " + user.getUserId());
			throw e;
		}
		return user;
	}
	
	public User findByUserId(String userId) {
		User user = null;
		try {
			Query query = getManager().createQuery("from User where userid = :userid");
			query.setParameter("userid", userId);
			
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning userid: " + userId);
		} catch (Exception e) {
			logger.error("Error returning userid: " + userId, e);
		}
		
		return user;
	}
	
	public User findByUserId(String userId, String loginService) {
		User user = null;
		try {
			Query query = getManager().createQuery("from User where userid = :userId and loginservice = :loginservice");
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
