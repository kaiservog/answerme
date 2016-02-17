package com.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "topic")
@SequenceGenerator(name = "topic_seq", sequenceName = "topic_id_seq", allocationSize = 1)
public class Topic implements Serializable {
	private static final long serialVersionUID = 6911000237717989580L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "topic_seq")
	@Column(name = "id_topic", unique = true, insertable = false, updatable = false)
	private long id;

	@Column(name = "name")
	private String name;

	public Topic() {
	}

	public Topic(String name) {
		super();
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
