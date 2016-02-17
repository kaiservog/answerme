package com.server.model;

public class QuestionView {

	private long id;
	private String topic;
	private String question;
	
	public QuestionView(Question question) {
		this.topic = question.getTopic().getName();
		this.question = question.getQuestion();
		this.id = question.getId();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	
	
}
