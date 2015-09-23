package com.bsb.user;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.ApplicationAware;

import com.bsb.entity.MsgEntity;
import com.bsb.entity.UserEntity;
import com.bsb.tools.RandomNumber;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport implements ApplicationAware{
	private UserService service = new UserService();
	private String phone;
	private Map<String,Object> application;
	private String id;
	private String name;
	private String password;
	private String authcode;
	private String role;
	private String skill;
	private List<MsgEntity> msgList;
	private String result = "yes";
	private String reason;
	
	/**
	 * 用户获取验证码
	 */
	public String getAuthCode(){
		//健壮性判断
		if(this.phone==null || "".equals(phone)){
			this.result = "no";
			this.reason = "phone不能为空";
			return "getAuthCode";
		}
		
		//生成验证码
		String code = RandomNumber.getFixLenthString(4);
		
		//存入application
		application.put(this.phone, code);
		
		System.out.println("验证码："+code);
		
		//存入DB，等待Android发送
		boolean result = service.saveCode_Phone(phone, code);
		if(result && service.getResult()){
			this.result = "yes";
			return "getAuthCode";
		}
		else{
			this.result = "no";
			this.reason = service.getReason();
			return "getAuthCode";
		}
	}

	
	/**
	 * 注册
	 * @return
	 */
	public String login(){
		//健壮性判断
		if(name==null || name.equals("") || password==null || password.equals("")
				|| role==null || role.equals("") || phone==null || phone.equals("") || authcode==null || authcode.equals("")){
			this.result = "no";
			this.reason = "name、password、role、phone、authcode不能为空";
			return "login";
		}
		
		//若为provider，需要传递skill
		if(role.equals("1")){
			if(skill==null || skill.equals("")){
				this.result = "no";
				this.reason = "role为1时，skill不能为空";
				return "login";
			}
		}
		
		//判断验证码是否正确
		String code = (String) application.get(phone);
		System.out.println("验证码："+code);
		//若application中的数据已丢失
		if(code==null || code.equals("")){
			this.result = "no";
			this.reason = "application中的数据已丢失，请重新获取验证码";
			return "login";
		}
		//若application中的验证码还存在，则进行比较
		else{
			//验证失败
			if(!code.equals(authcode)){
				this.result = "no";
				this.reason = "验证码错误";
				return "login";
			}
			//验证成功
			else{
				//开始注册
				UserEntity entity = new UserEntity();
				entity.setName(name);
				entity.setPassword(password);
				entity.setPhone(phone);
				if(role.equals("0"))
					entity.setRole(0);
				else
					entity.setRole(1);
				entity.setSkill(skill);
				int id = service.saveUserEntity(entity);
				
				//保存失败
				if(id<=0){
					this.result = "no";
					this.reason = service.getReason();
					return "login";
				}
				//注册成功
				else{
					this.result = "yes";
					this.id = id+"";
					//删掉Application中的健值对
					application.remove(this.phone);
				}
				return "login";
			}
		}
	}
	
	
	
	/**
	 * 登录
	 * @return
	 */
	public String signin(){
		if(password==null || password.equals("") || phone==null || phone.equals("")){
			this.result = "no";
			this.reason = "password、phone不能为空";
			return "signin";
		}
		
		//登录鉴权
		UserEntity entity = service.signin(phone, password);
		
		//登录结果判断
		//登录失败
		if(entity==null || !service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
			return "signin";
		}
		//登录成功
		else{
			this.id = entity.getId()+"";
			this.name = entity.getName();
			this.phone = entity.getPhone();
			this.role = entity.getRole()+"";
			this.skill = entity.getSkill();
			this.result = "yes";
		}
		return "signin";
	}
	
	
	/**
	 * (Android)获取未发送的短信验证码
	 * @return
	 */
	public String getUnSendCodeList(){
		//获取短信
		this.msgList = service.queryUnSendAuthCodeMsgList();
		
		//判断结果
		//若获取短息你失败
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		//若获取成功
		else{
			this.result = "yes";
		}
		return "getUnSendCodeList";
	}
	
	
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getSkill() {
		return skill;
	}


	public void setSkill(String skill) {
		this.skill = skill;
	}


	public Map<String, Object> getApplication() {
		return application;
	}


	public String getAuthcode() {
		return authcode;
	}


	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}


	public List<MsgEntity> getMsgList() {
		return msgList;
	}


	public void setMsgList(List<MsgEntity> msgList) {
		this.msgList = msgList;
	}
	
	
}
