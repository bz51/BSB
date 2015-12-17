package com.bsb.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;

import com.bsb.core.CoreDao;
import com.bsb.core.HttpRequest;
import com.bsb.core.Parameter;
import com.bsb.entity.OpenTokenId;
import com.bsb.entity.UserEntity;
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
	private String access_token;

	private Map<String,Object> application;
	
	
	/**
	 * 获取Parameter中存储的access_token
	 * @return
	 */
	public String getAccessToken(){
		this.setAccess_token(Parameter.AccessToken_Parameters);
		return "getAccessToken";
	}
	
	
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
//		OpenTokenId entity = new OpenTokenId();
//		entity.setOpen_id(open_id);
//		entity.setOpen_token(open_token);
//		CoreDao.save(entity);
		//open_token＋open_id存储在Parameter中！！！
		Parameter.OpenTokenId_Parameters.put(open_token, open_id);
		
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
//		OpenTokenId entity = new OpenTokenId();
//		entity.setOpen_id(open_id);
//		entity.setOpen_token(open_token);
//		CoreDao.save(entity);
		//open_token＋open_id存储在Parameter中！！！
		Parameter.OpenTokenId_Parameters.put(open_token, open_id);
		return "auth_return_login";
	}
	
	
	
	/**
	 * 微信模板消息推送测试
	 * @param args
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws JSONException{
		WxTemplate t = new WxTemplate();
		t.setUrl("www.5188.help");
		//接受者的open_id
//		t.setTouser("o6uVGv8OUlCv-OrgeK5bWBV-6i_E");//柴
		t.setTouser("o6uVGvwNbicJU6QmFqZTzU-LlOBA");//杨罗坤
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id("rhTqtWR1H0hfFI3DQAZLySeetoaz15f5G6969IhZW3Q");
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("#000000");
		first.setValue("紧急通知！！！");
		m.put("first", first);
		//订单编号字段
		TemplateData orderSn = new TemplateData();
		orderSn.setColor("#000000");
		orderSn.setValue("您的手机即将爆炸");
		m.put("OrderSn", orderSn);
		//订单状态字段
		TemplateData orderStatus = new TemplateData();
		orderStatus.setColor("#000000");
		orderStatus.setValue("杨罗坤的手机即将爆炸");
		m.put("OrderStatus", orderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("blue");
		remark.setValue("杨罗坤的手机即将爆炸");
		m.put("remark", remark);
		t.setData(m);
		
		//获取access_token
		String param = "grant_type=client_credential&appid=wx1a4c2e86c17d1fc4&secret=ba95beea6a08dc2abf245ae8d8c7dc3e";
		String access_token_result = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", param);
		String access_token = new JSONObject(access_token_result).getString("access_token");
		System.out.println("access_token="+access_token);
		
		//将模板消息转换成JSON
//		JSONObject temJson = new JSONObject(t);
		
		//发送模板消息
		for(int i=0;i<100;i++){
			String result = HttpRequest.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token,new JSONObject(t).toString());
			System.out.println(i+1+",result="+result);
		}
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


	public String getAccess_token() {
		return access_token;
	}


	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
}
