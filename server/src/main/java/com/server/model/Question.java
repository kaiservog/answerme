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
	private int id;
	
	@OneToOne()
	@JoinColumn(name = "querist")
	private User querist;
	@OneToOne()
	@JoinColumn(name = "responder")
	private User responder;
	@OneToOne()
	@JoinColumn(name ="tag")
	private Tag tag;
	
	@Column(name = "question")
	private String question;
	@Column(name = "answer")
	private String answer;

	public Question() {
	}
	
	public Question(User querist, User responder, Tag tag, String question, String answer) {
		super();
		this.querist = querist;
		this.responder = responder; 
		this.tag = tag;
		this.question = question;
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
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
}
