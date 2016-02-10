package com.server.service;

import java.util.List;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.server.conf.RedisClient;
import com.server.controller.QuestionController;
import com.server.model.Question;

public class QuestionTtlService implements Job {
	
	@Inject
	private QuestionController questionController;
	
	@Inject
	private RedisClient redisClient;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<Question> expiredQuestions = questionController.updateExpiredQuestions();
		
		for (Question question : expiredQuestions) {
			redisClient.getJedis().lpush(question.getTopic().getName(), String.valueOf(question.getId()));
		}
	}
}
