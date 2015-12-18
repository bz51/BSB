package com.bsb.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;

import com.bsb.core.CoreDao;
import com.bsb.core.HttpRequest;
import com.bsb.core.Parameter;
import com.bsb.core.SMS;
import com.bsb.entity.MsgEntity;
import com.bsb.entity.OpenTokenId;
import com.bsb.entity.UserEntity;
import com.bsb.tools.RandomNumber;
import com.bsb.wechat.WeChatAction;
import com.opensymphony.xwork2.ActionSupport;
import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

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
	private String open_token;
	private String open_id;
	private List<MsgEntity> msgList;
	private String result = "yes";
	private String reason;
	
	//微信验证Token
	private String signature;
	private String timestamp;
	private String nonce;
	private String echostr;
	
	private String state;
	private String code;
	
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
		
		System.out.println("验证码："+application.get(this.phone));
		
		//存入DB，等待Android发送(已改成使用alidayu发送短信)
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
	 * @throws JSONException 
	 */
	public String login() throws JSONException{
		//1.健壮性判断
		if(name==null || name.equals("") || password==null || password.equals("")
				|| role==null || role.equals("") || phone==null || phone.equals("") || authcode==null || authcode.equals("")){
			this.result = "no";
			this.reason = "name、password、role、phone、authcode不能为空";
			return "login";
		}
		
		//1.2.若为provider，需要传递skill
		if(role.equals("1")){
			if(skill==null || skill.equals("")){
				this.result = "no";
				this.reason = "role为1时，skill不能为空";
				return "login";
			}
		}
		
		//2.判断验证码是否正确
		//2.1获取application中的验证码
		String code = (String) application.get(phone);
		System.out.println("验证码："+code);
		
		//2.2判断application中验证码是否存在
		if(code==null || code.equals("")){
			this.result = "no";
			this.reason = "application中的数据已丢失，请重新获取验证码";
			return "login";
		}
		//2.3若application中的验证码还存在，则进行比较
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
				
//				//从数据库中取open_token对应的open_id
//				List<String> list = new ArrayList<String>();
//				list = CoreDao.queryListByHql("select open_id from OpenTokenId where open_token='"+open_token+"'");
////				SMS.sendMsg("open_token="+open_token+"open_id="+list.get(0), "15251896025");
//				//open_token不存在，则返回“open_token is missing”，客户端需重新授权
//				if(list.size()==0){
//					this.result = "no";
//					this.reason = "open_token is missing";
//					return "login";
//				}
//				
//				//open_token存在
//				else{
//					System.out.println("open_id="+list.get(0));
//					this.open_id = list.get(0);
//					entity.setWeixin_id(open_id);
//				}
				
				//从Parameter中取open_token对应的open_id
				this.open_id = Parameter.OpenTokenId_Parameters.get(open_token);
				//open_token不存在，则返回“open_token is missing”，客户端需重新授权
				if(open_id==null || "".equals(open_id)){
					this.result = "no";
					this.reason = "open_token is missing";
					return "login";
				}
				
				//open_token存在
				else{
					System.out.println("open_id="+open_id);
					entity.setWeixin_id(open_id);
				}
				
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
//					application.remove(this.open_token);
					//删除Parameter中的open_id和open_token
					Parameter.OpenTokenId_Parameters.remove(open_token);
					//微信通知管理员
					WeChatAction.sendToAllAdmin_newUser(entity);
				}
				return "login";
			}
		}
	}
	
	public String test(){
//		application.put(open_token, "chai");
		String keys = "";
		Object[] arr = application.keySet().toArray();
		for(int i=0;i<arr.length;i++)
			keys = keys+"," + arr[i].toString().substring(0,5);
		SMS.sendMsg(keys, "15251896025");
//		this.open_id = application.get(open_token);
		return "test";
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
		
//		//从数据库中取open_token对应的open_id
//		List<String> list = new ArrayList<String>();
//		list = CoreDao.queryListByHql("select open_id from OpenTokenId where open_token='"+open_token+"'");
//		//open_token不存在，则返回“open_token is missing”，客户端需重新授权
//		if(list.size()==0){
//			this.result = "no";
//			this.reason = "open_token is missing";
//			return "signin";
//		}
//		//open_token存在
//		else{
//			System.out.println("open_id="+list.get(0));
//			open_id = list.get(0);
//		}
		
		// 从Parameter中取open_token对应的open_id]
		open_id = Parameter.OpenTokenId_Parameters.get(open_token);
		// open_token不存在，则返回“open_token is missing”，客户端需重新授权
		if (open_id==null || "".equals(open_id)) {
			this.result = "no";
			this.reason = "open_token is missing";
			return "signin";
		}
		// open_token存在
		else {
			System.out.println("open_id=" + open_id);
		}
		
		//登录鉴权(这里包含了将open_id存入user表)
		UserEntity entity = service.signin(phone, password,open_id);
		
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
			//open_id是全局变量，已经获取到了，无需再赋值
			this.result = "yes";
			//删除Parameter中的open_id和open_token
			Parameter.OpenTokenId_Parameters.remove(open_token);
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
	
	
	/**
	 * 用于验证微信Token
	 * @throws IOException 
	 */
	public void authToken() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");//½â¾öÖÐÎÄÂÒÂë
		PrintWriter out = response.getWriter();
		out.println(echostr);
		out.flush();
		out.close();
	}
	
	
	/**
	 * 微信授权(获取open_id后返回首页)
	 * @throws JSONException 
	 */
	public String auth_return_index() throws JSONException{
		//进入这个接口的时候就已经获取到code、state
		//获取open_token
		String open_token = state;
		
		//通过code获取open_id
		String result = HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", "appid=wx1a4c2e86c17d1fc4&secret=ba95beea6a08dc2abf245ae8d8c7dc3e&code="+code+"&grant_type=authorization_code");
		JSONObject json_result = new JSONObject(result);
		String open_id = json_result.getString("openid");
		
		//open_token＋open_id存储在数据库中！！！
		OpenTokenId entity = new OpenTokenId();
		entity.setOpen_id(open_id);
		entity.setOpen_token(open_token);
		CoreDao.save(entity);
		
		//将open_token和open_id发送到chaibozhou手机上
		//这里open_id都已经有了！applicaiton中也取出来了！
//		SMS.sendMsg("app中open_id="+application.get(open_token)+",open_token="+open_token+"", "15251896025");
		return "auth_return_index";
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


//	public Map<String, Object> getApplication() {
//		return application;
//	}


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


	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public String getNonce() {
		return nonce;
	}


	public void setNonce(String nonce) {
		this.nonce = nonce;
	}


	public String getEchostr() {
		return echostr;
	}


	public void setEchostr(String echostr) {
		this.echostr = echostr;
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


	public Map<String, Object> getApplication() {
		return application;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	
	
	
}
