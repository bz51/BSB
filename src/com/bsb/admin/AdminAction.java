package com.bsb.admin;

import java.util.List;

import com.bsb.entity.FeedBackEntity;
import com.bsb.entity.UserEntity;
import com.opensymphony.xwork2.ActionSupport;

public class AdminAction extends ActionSupport{
	private List<UserEntity> userList;
	private List<FeedBackEntity> feedbackList;
	private String password;
	private String feedback_id;
	private String require_id;
	private String result = "yes";
	private String reason;
	private AdminService service = new AdminService();
	
	/**
	 * 获取所有用户信息
	 */
	public String getAllUser(){
		if(password==null || "".equals(password) || !password.equals("1314")){
			this.result = "no";
			this.reason = "口令错误";
			return "getAllUser";
		}
		
		this.userList = service.getAllUser();
		return "getAllUser";
	}
	
	/**
	 * 获取所有反馈信息
	 */
	public String getFeedback(){
		if(password==null || "".equals(password) || !password.equals("1314")){
			this.result = "no";
			this.reason = "口令错误";
			return "getFeedback";
		}
		
		this.feedbackList = service.getFeedback();
		return "getFeedback";
	}
	
	/**
	 * 处理一条反馈信息
	 */
	public String doFeedback(){
		if(password==null || "".equals(password) || feedback_id==null || "".equals(feedback_id) || require_id==null || "".equals(require_id)){
			this.result = "no";
			this.reason = "password、feedback_id、require_id均不能为空！";
			if(!password.equals("1314"))
				this.reason = "口令错误";
			return "doFeedback";
		}
		
		service.doFeedback(this.feedback_id,this.require_id);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		
		return "doFeedback";
	}

	public List<UserEntity> getUserList() {
		return userList;
	}

	public void setUserList(List<UserEntity> userList) {
		this.userList = userList;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<FeedBackEntity> getFeedbackList() {
		return feedbackList;
	}

	public void setFeedbackList(List<FeedBackEntity> feedbackList) {
		this.feedbackList = feedbackList;
	}

	public String getFeedback_id() {
		return feedback_id;
	}

	public void setFeedback_id(String feedback_id) {
		this.feedback_id = feedback_id;
	}

	public String getRequire_id() {
		return require_id;
	}

	public void setRequire_id(String require_id) {
		this.require_id = require_id;
	}


	
	
	
}
