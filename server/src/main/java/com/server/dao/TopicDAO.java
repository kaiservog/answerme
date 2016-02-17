package com.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.db.TransactionRequired;
import com.server.model.Topic;

public class TopicDAO extends Dao<Topic> {
	private static final Logger logger = LoggerFactory.getLogger(TopicDAO.class);

	@TransactionRequired
	public void add(Topic topic) {
		try {
			getManager().persist(topic);
			getManager().getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error inserting topicName: " + topic.getName());
			getManager().getTransaction().rollback();
		}
	}
	
	public List<Topic> findByTopicsName(List<String> names) {
		TypedQuery<Topic> query = getManager().createQuery("from Topic where text in :names", Topic.class);
		query.setParameter("names", names);

		return query.getResultList();
	}

	@TransactionRequired
	public List<Topic> findOrPersist(List<String> stringTopics) {
		List<Topic> result = new ArrayList<Topic>();
		
		for (String stringTopic : stringTopics) {
			TypedQuery<Topic> query = getManager().createQuery("from Topic where name = :name", Topic.class);
			query.setParameter("name", stringTopic);
			Topic topic = null;
			try {
				topic = query.getSingleResult();
			} catch (NoResultException e) {
				topic = new Topic(stringTopic);
				getManager().persist(topic);
			}

			result.add(topic);
		}
		return result;
	}
	

	public Topic findByName(String name) {
		Topic topic = null;
		try {
			TypedQuery<Topic> query = getManager().createQuery("from Topic where name = :name", Topic.class);
			query.setParameter("name", name);
			
			topic = (Topic) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning topic: " + name);
		} catch (Exception e) {
			logger.error("Error returning topic: " + name, e);
		}
		
		return topic;
	}
}
