package com.server.controller;

import javax.inject.Inject;

import com.server.dao.UserDAO;
import com.server.model.User;

public class UserController {

	@Inject private UserDAO userDAO;
	 
    public User add(User user) {
    	return userDAO.add(user);
    }
    
    public User get(String userId, String loginService) {
    	return userDAO.findByUserId(userId, loginService);
    }
    
    public User getByUserId(String userId, String loginService) {
    	return userDAO.findByUserId(userId, loginService);
    }
    
    public void persist(User user) {
    	userDAO.persist(user);
    }
}
