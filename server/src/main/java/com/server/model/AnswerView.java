package com.server.model;

public class AnswerView extends QuestionView {
	private String answer;
	
	public AnswerView(Question question) {
		super(question);
		this.answer = question.getAnswer();
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
