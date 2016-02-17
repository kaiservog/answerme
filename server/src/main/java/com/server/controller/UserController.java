package com.server.controller;

import javax.inject.Inject;

import com.server.dao.UserDAO;
import com.server.model.User;

public class UserController {

	@Inject private UserDAO userDAO;
	 
    public User add(User user) {
    	return userDAO.add(user);
    }
    
    public User getByExternalUserId(String externalUserId, String loginService) {
    	return userDAO.findByExternalUserId(externalUserId, loginService);
    }
    
    public User getById(long id) {
    	return userDAO.findById(id);
    }
    
    public User getById(String id) {
    	return userDAO.findById(Long.valueOf(id));
    }
    
    public void persist(User user) {
    	userDAO.persist(user);
    }
}
