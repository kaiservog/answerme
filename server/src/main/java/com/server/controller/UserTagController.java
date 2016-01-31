package com.server.controller;

import javax.inject.Inject;

import com.server.dao.UserTagDAO;
import com.server.model.User;
import com.server.model.UserTag;

public class UserTagController {

	@Inject private UserTagDAO userTagDao;
	 
    public void add(UserTag userTag) {
    	userTagDao.add(userTag);
    }
    
    public UserTag findByUser(User user) {
    	return userTagDao.findByUser(user);
    }
}
