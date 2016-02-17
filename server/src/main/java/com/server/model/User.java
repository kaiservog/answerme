package com.server.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
	@Column(name = "id_user", unique = true, insertable = false, updatable = false)
	private long id;

	@Column(name = "externaluserid")
	private String externalUserId;

	@Column(name = "loginservice")
	private String loginService;

	@Column(name = "firstname")
	private String firstName;

	@ManyToMany
	@JoinTable(name = "user_topic", joinColumns = { @JoinColumn(name = "id_user") }, inverseJoinColumns = { @JoinColumn(name = "id_topic") })
	private List<Topic> topics;

	public User() {
	}

	public User(String externalUserId, String loginService, String firstName) {
		super();
		this.externalUserId = externalUserId;
		this.loginService = loginService;
		this.firstName = firstName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLoginService() {
		return loginService;
	}

	public void setLoginService(String loginService) {
		this.loginService = loginService;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public String getExternalUserId() {
		return externalUserId;
	}

	public void setExternalUserId(String externalUserId) {
		this.externalUserId = externalUserId;
	}

}
