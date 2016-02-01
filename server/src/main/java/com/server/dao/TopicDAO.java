package com.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.server.model.Topic;

public class TopicDAO extends Dao {

	public List<Topic> findByTopicsText(List<String> texts) {
		TypedQuery<Topic> query = getManager().createQuery("from Topic where text in :texts", Topic.class);
		query.setParameter("texts", texts);

		return query.getResultList();
	}

	public List<Topic> findOrPersist(List<String> stringTopics) {
		List<Topic> result = new ArrayList<Topic>();
		getManager().getTransaction().begin();
		
		for (String stringTopic : stringTopics) {
			TypedQuery<Topic> query = getManager().createQuery("from Topic where text = :text", Topic.class);
			query.setParameter("text", stringTopic);
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
}
