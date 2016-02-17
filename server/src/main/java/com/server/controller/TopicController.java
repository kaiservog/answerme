package com.server.controller;

import java.util.List;

import javax.inject.Inject;

import com.server.dao.TopicDAO;
import com.server.model.Topic;

public class TopicController {
	@Inject
	private TopicDAO topicDao;

	public void add(Topic topic) {
		topicDao.add(topic);
	}
	public List<Topic> findByTopicsName(List<String> name) {
		return topicDao.findByTopicsName(name);
	}

	public List<Topic> findOrPersist(List<String> stringTopics) {
		return topicDao.findOrPersist(stringTopics);
	}
	
	public Topic findByName(String name) {
		return topicDao.findByName(name);
	}
}
