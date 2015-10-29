package com.bsb.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="open_token_id")
public class OpenTokenId {
	private int id;
	private String open_token;
	private String open_id;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOpen_token() {
		return open_token;
	}
	public void setOpen_token(String open_token) {
		this.open_token = open_token;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	
	
}
