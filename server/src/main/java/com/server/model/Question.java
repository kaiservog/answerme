package com.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "question")
@SequenceGenerator(name = "question_seq", sequenceName = "question_id_seq", allocationSize = 1)
public class Question implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "question_seq")
	@Column(name = "id_question", unique = true, insertable = false, updatable = false)
	private long id;
	
	@OneToOne()
	@JoinColumn(name = "querist")
	private User querist;
	@OneToOne()
	@JoinColumn(name = "responder")
	private User responder;
	@OneToOne()
	@JoinColumn(name = "topic")
	private Topic topic;
	@Column(name = "question")
	private String question;
	@Column(name = "answer")
	private String answer;
	@Column(name = "ttl")
	private long ttl;

	public Question() {
	}
	
	public Question(User querist, User responder, Topic topic, String question, String answer, long ttl) {
		super();
		this.querist = querist;
		this.responder = responder; 
		this.topic = topic;
		this.question = question;
		this.answer = answer;
		this.ttl = ttl;
	}

	public void accept(User user) {
		this.setResponder(user);
		this.setTtl(System.currentTimeMillis());
	}
	
	public void reject() {
		this.setTtl(0);
	}
	
	public void answer(String answer) {
		this.setTtl(0);
		this.setAnswer(answer);
	}
	
	public void resetTtl() {
		this.setTtl(System.currentTimeMillis());
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getQuerist() {
		return querist;
	}

	public void setQuerist(User querist) {
		this.querist = querist;
	}

	public User getResponder() {
		return responder;
	}

	public void setResponder(User responder) {
		this.responder = responder;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
}
