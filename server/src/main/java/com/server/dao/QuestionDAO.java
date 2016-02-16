package com.server.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.model.Question;
import com.server.model.User;


public class QuestionDAO {
	@Inject
	private EntityManager manager;
	
	private static final Logger logger = LoggerFactory.getLogger(QuestionDAO.class);

	public void add(Question question) {
		manager.getTransaction().begin();
		try {
			manager.persist(question);
			manager.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error inserting question: " + question.getQuestion());
			manager.getTransaction().rollback();
		}
	}
	
	public void update(Question question) {
		manager.getTransaction().begin();
		try {
			manager.merge(question);
			manager.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error updating question: " + question.getQuestion());
			manager.getTransaction().rollback();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Question> findExpiredQuestionsNotAccepted(long time) {
		List<Question> questions = null;
		try {
			Query query = manager.createQuery("from Question where ttl < :time and responder is null");
			query.setParameter("time", time);
			questions = query.getResultList();
		} catch (NoResultException e) {
			logger.error("Error returning questions: ", e.getMessage());
		} catch (Exception e) {
			logger.error("Error returning questions: ", e);
		}
		
		return questions;
	}
	
	public void updateExpiredQuestionsNotAccepted(long time) {
		manager.getTransaction().begin();
		try {
			Query query = manager.createQuery("update Question set ttl = 0, responder = null where ttl < :time and responder is null");
			query.setParameter("time", time);
			query.executeUpdate();
			manager.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error updating expired questions: ", e.getMessage());
			manager.getTransaction().rollback();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Question> findExpiredQuestionsAccepted(long time) {
		List<Question> questions = null;
		try {
			Query query = manager.createQuery("from Question where ttl < :time and responder is not null");
			query.setParameter("time", time);
			questions = query.getResultList();
		} catch (NoResultException e) {
			logger.error("Error returning questions: ", e.getMessage());
		} catch (Exception e) {
			logger.error("Error returning questions: ", e);
		}
		
		return questions;
	}
	
	public void updateExpiredQuestionsAccepted(long time) {
		manager.getTransaction().begin();
		try {
			Query query = manager.createQuery("update Question set ttl = 0, responder = null where ttl < :time and responder is not null");
			query.setParameter("time", time);
			query.executeUpdate();
			manager.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error updating expired questions: ", e.getMessage());
			manager.getTransaction().rollback();
		}
	}
	
	public Question find(Long questionId) {
		Question question = null;
		try {
			question = manager.find(Question.class, questionId);
		} catch (Exception e) {
			logger.error("Error returning question: " + questionId);
		}
		return question;
	}
	
	public Question findByUser(User user) {
		Question question = null;
		try {
			Query query = manager.createQuery("from Question where user = :user");
			query.setParameter("user", user);
			
			question = (Question) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error returning question: " + question.getQuestion());
		} catch (Exception e) {
			logger.error("Error returning question: " + question.getQuestion(), e);
		}
		
		return question;
	}
}
