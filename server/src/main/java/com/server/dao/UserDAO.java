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
			logger.error("Error inserting userid: " + user.getExternalUserId());
			throw e;
		}
		return user;
	}
	
	public User findByExternalUserId(String externalUserId, String loginService) {
		User user = null;
		try {
			Query query = getManager().createQuery("from User where externaluserid = :externalUserId and loginservice = :loginservice");
			query.setParameter("externalUserId", externalUserId);
			query.setParameter("loginservice", loginService);
			
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning userId: " + externalUserId);
		} catch (Exception e) {
			logger.error("Error returning userId: " + externalUserId, e);
		}
		
		return user;
	}
	
	public User findById(long id) {
		User user = null;
		try {
			Query query = getManager().createQuery("from User where id = :id");
			query.setParameter("id", id);
			
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning userId: " + id);
		} catch (Exception e) {
			logger.error("Error returning userId: " + id, e);
		}
		
		return user;
	}
}
