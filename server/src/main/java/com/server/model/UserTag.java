package com.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "usertag")
@SequenceGenerator(name = "usertag_seq", sequenceName = "usertag_id_seq", allocationSize = 1)
public class UserTag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "usertag_seq")
	@Column(name = "id_usertag", unique = true, insertable = false, updatable = false)
	private int id;
	
	@JoinColumn()
	private User user;
	@JoinColumn()
	private Tag tag;

	public UserTag() {
	}

	public UserTag(User user, Tag tag) {
		super();
		this.user = user;
		this.tag = tag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
