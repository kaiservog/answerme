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
@Table(name = "user")
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
	@Column(name = "id_user", unique = true, insertable = false, updatable = false)
	private int id;
	@Column(name = "firstname")
	private String firstName;
	@Column(name = "lastName")
	private String lastName;
	@Column(name = "phone")
	private String phone;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;

	public User() {
	}

	public User(String firstName, String lastName, String phone, String username, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.username = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
