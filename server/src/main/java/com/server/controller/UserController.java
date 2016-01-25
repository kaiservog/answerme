package com.server.controller;

import javax.inject.Inject;

import com.server.dao.UserDAO;
import com.server.model.User;

public class UserController {

	@Inject private UserDAO userDAO;
	 
    public void add(User user) {
    	userDAO.add(user);
    }
    
    public User get(String username) {
    	return userDAO.findByUsername(username);
    }
}
