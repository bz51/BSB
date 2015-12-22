package com.bsb.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="need")
public class NeedEntity {
	private int id;
	private String needer_weixin;
	private int needer_id;
	private String needer_name;
	private String needer_phone;
	private String needer_skill;
	private int money;
	private String title;
	private String content;
	private Timestamp time;
	private String provider_weixin;
	private int provider_id;
	private String provider_name;
	private String provider_phone;
	private String provider_skill;
	private int post;
	private int state;
	private Timestamp deadline;
	private String contract;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNeeder_weixin() {
		return needer_weixin;
	}
	public void setNeeder_weixin(String needer_weixin) {
		this.needer_weixin = needer_weixin;
	}
	public int getNeeder_id() {
		return needer_id;
	}
	public void setNeeder_id(int needer_id) {
		this.needer_id = needer_id;
	}
	public String getNeeder_name() {
		return needer_name;
	}
	public void setNeeder_name(String needer_name) {
		this.needer_name = needer_name;
	}
	public String getNeeder_phone() {
		return needer_phone;
	}
	public void setNeeder_phone(String needer_phone) {
		this.needer_phone = needer_phone;
	}
	public String getNeeder_skill() {
		return needer_skill;
	}
	public void setNeeder_skill(String needer_skill) {
		this.needer_skill = needer_skill;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getProvider_weixin() {
		return provider_weixin;
	}
	public void setProvider_weixin(String provider_weixin) {
		this.provider_weixin = provider_weixin;
	}
	public int getProvider_id() {
		return provider_id;
	}
	public void setProvider_id(int provider_id) {
		this.provider_id = provider_id;
	}
	public String getProvider_name() {
		return provider_name;
	}
	public void setProvider_name(String provider_name) {
		this.provider_name = provider_name;
	}
	public String getProvider_phone() {
		return provider_phone;
	}
	public void setProvider_phone(String provider_phone) {
		this.provider_phone = provider_phone;
	}
	public String getProvider_skill() {
		return provider_skill;
	}
	public void setProvider_skill(String provider_skill) {
		this.provider_skill = provider_skill;
	}
	public int getPost() {
		return post;
	}
	public void setPost(int post) {
		this.post = post;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Timestamp getDeadline() {
		return deadline;
	}
	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	
	
}
