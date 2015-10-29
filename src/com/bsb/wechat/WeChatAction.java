package com.bsb.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;

import com.bsb.core.CoreDao;
import com.bsb.core.HttpRequest;
import com.bsb.core.SMS;
import com.bsb.entity.OpenTokenId;
import com.opensymphony.xwork2.ActionSupport;
import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

public class WeChatAction extends ActionSupport implements ApplicationAware{	
	//微信验证Token
	private String signature;
	private String timestamp;
	private String nonce;
	private String echostr;
	
	private String openid;
	private String code;
	private String state;

	private Map<String,Object> application;
	
	/**
	 * 用于验证微信Token
	 * @throws IOException 
	 */
	public void authToken() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
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
		
		return "auth_return_index";
	}
	
	
	/**
	 * 微信授权(获取open_id后返回登录页)
	 * @throws JSONException 
	 */
	public String auth_return_login() throws JSONException{
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
		return "auth_return_login";
	}
	
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
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
	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}
	public Map<String, Object> getApplication() {
		return application;
	}
	
}
