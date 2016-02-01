package com.server.controller;

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
}
