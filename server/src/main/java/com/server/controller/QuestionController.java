package com.server.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.server.dao.QuestionDAO;
import com.server.model.Question;

public class QuestionController {

	@Inject private QuestionDAO questionDao;
	 
    public void add(Question question) {
    	questionDao.add(question);
    }
    
    public Question find(Integer questionId) {
    	return questionDao.find(questionId);
    }
    
    public void update(Question question) {
    	questionDao.update(question);
    }
    
    public List<Question> updateExpiredQuestions() {
    	List<Question> expiredQuestions = new ArrayList<>();
    	
    	long time = System.currentTimeMillis() - 120000;
    	expiredQuestions = questionDao.findExpiredQuestionsNotAccepted(time);
    	questionDao.updateExpiredQuestionsNotAccepted(time);
    	
    	time = System.currentTimeMillis() - 600000;
    	expiredQuestions.addAll(questionDao.findExpiredQuestionsAccepted(time));
    	questionDao.updateExpiredQuestionsAccepted(time);
    	
    	return expiredQuestions;
    }
}
