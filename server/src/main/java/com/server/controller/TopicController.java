package com.server.controller;

import java.util.List;

import javax.inject.Inject;

import com.server.dao.TopicDAO;
import com.server.model.Topic;

public class TopicController {
	@Inject
	private TopicDAO topicDao;

	public List<Topic> findByTopicsText(List<String> texts) {
		return topicDao.findByTopicsText(texts);
	}

	public List<Topic> findOrPersist(List<String> stringTopics) {
		return topicDao.findOrPersist(stringTopics);
	}
}
