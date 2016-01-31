package com.server.controller;

import javax.inject.Inject;

import com.server.dao.TagDAO;
import com.server.model.Tag;

public class TagController {

	@Inject private TagDAO tagDao;
	 
    public void add(Tag tag) {
    	tagDao.add(tag);
    }
    
    public Tag findByName(String name) {
    	return tagDao.findByName(name);
    }
}
