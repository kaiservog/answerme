package com.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.model.Topic;

public class TopicDAO extends Dao {
	private static final Logger logger = LoggerFactory.getLogger(TopicDAO.class);

	public void add(Topic tag) {
		getManager().getTransaction().begin();
		try {
			getManager().persist(tag);
			getManager().getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error inserting tagname: " + tag.getName());
			getManager().getTransaction().rollback();
		}
	}
	
	public List<Topic> findByTopicsName(List<String> names) {
		TypedQuery<Topic> query = getManager().createQuery("from Topic where text in :names", Topic.class);
		query.setParameter("names", names);

		return query.getResultList();
	}

	public List<Topic> findOrPersist(List<String> stringTopics) {
		List<Topic> result = new ArrayList<Topic>();
		getManager().getTransaction().begin();
		
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
		getManager().getTransaction().commit();
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
