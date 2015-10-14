package com.bsb.admin;

import java.util.List;

import com.bsb.entity.UserEntity;
import com.opensymphony.xwork2.ActionSupport;

public class AdminAction extends ActionSupport{
	private List<UserEntity> userList;
	private String password;
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
	
	
	
}
