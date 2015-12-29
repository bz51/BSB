package com.bsb.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.bsb.core.HttpRequest;
import com.bsb.core.MD5;
import com.bsb.core.Parameter;
import com.bsb.entity.UserEntity;
import com.bsb.tools.RandomNumber;
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
	private String ticket;//用于JS－SDK
	private String prepay_id;
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
//		WxTemplate t = new WxTemplate();
//		t.setUrl("www.5188.help");
//		//接受者的open_id
////		t.setTouser("o6uVGv8OUlCv-OrgeK5bWBV-6i_E");//柴
////		t.setTouser("o6uVGvwNbicJU6QmFqZTzU-LlOBA");//杨罗坤
//		t.setTouser("o6uVGv89frbuhh3qFlatwKKKzEro");//周晓兵
//		//消息顶端颜色
//		t.setTopcolor("#000000");
//		//模板ID
//		t.setTemplate_id("rhTqtWR1H0hfFI3DQAZLySeetoaz15f5G6969IhZW3Q");
//		//存储数据的Map
//		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
//		//first字段
//		TemplateData first = new TemplateData();
//		first.setColor("#000000");
//		first.setValue("紧急通知！！！");
//		m.put("first", first);
//		//订单编号字段
//		TemplateData orderSn = new TemplateData();
//		orderSn.setColor("#000000");
//		orderSn.setValue("您的手机即将爆炸");
//		m.put("OrderSn", orderSn);
//		//订单状态字段
//		TemplateData orderStatus = new TemplateData();
//		orderStatus.setColor("#000000");
//		orderStatus.setValue("杨罗坤的手机即将爆炸");
//		m.put("OrderStatus", orderStatus);
//		//remark字段
//		TemplateData remark = new TemplateData();
//		remark.setColor("blue");
//		remark.setValue("杨罗坤的手机即将爆炸");
//		m.put("remark", remark);
//		t.setData(m);
//		
//		//获取access_token
//		String param = "grant_type=client_credential&appid=wx1a4c2e86c17d1fc4&secret=ba95beea6a08dc2abf245ae8d8c7dc3e";
//		String access_token_result = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", param);
//		String access_token = new JSONObject(access_token_result).getString("access_token");
//		System.out.println("access_token="+access_token);
//		
//		//将模板消息转换成JSON
////		JSONObject temJson = new JSONObject(t);
//		
//		//发送模板消息
//		for(int i=0;i<100;i++){
//			String result = HttpRequest.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token,new JSONObject(t).toString());
//			System.out.println(i+1+",result="+result);
//		}
		
		
//		UserEntity entity = new UserEntity();
//		entity.setName("柴毛毛");
//		entity.setPhone("15251896025");
//		entity.setRole(1);
//		entity.setTime(new Timestamp(new Date().getTime()));
//		sendToAdmin_newUser(entity,Parameter.OpenId_Chai);
//		sendToAdmin_newUser(entity,Parameter.OpenId_Yang);
//		sendToAdmin_newUser(entity,Parameter.OpenId_Zhou);
//		sendToAdmin_newUser(entity,Parameter.OpenId_Baba);
		
		
		
//		getPrepayId();
	}
	
	/**
	 * 新用户注册成功后向所有管理员发送模板消息 
	 * @throws JSONException 
	 */
	public static boolean sendToAllAdmin_newUser(UserEntity entity) throws JSONException{
		//给柴博周发消息
		sendToAdmin_newUser(entity,Parameter.OpenId_Chai);
		//给周晓兵发消息
		sendToAdmin_newUser(entity,Parameter.OpenId_Zhou);
		//给爸爸发消息
		sendToAdmin_newUser(entity,Parameter.OpenId_Baba);
		return true;
	}

	/**
	 * 新用户注册成功后向指定管理员发送模板消息 
	 * @param entity
	 * @param toUserOpenId 指定管理员的open_id
	 * @return
	 * @throws JSONException
	 */
	private static boolean sendToAdmin_newUser(UserEntity entity,String toUserOpenId) throws JSONException{
		WxTemplate t = new WxTemplate();
		t.setUrl("www.5188.help/admin.html");
		//接受者的open_id
		t.setTouser(toUserOpenId);
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id("j7lwc2R1bALL6WrIi7Igm7OcFJMngnY6tAuW1WfK11M");
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
//		TemplateData first = new TemplateData();
//		first.setColor("red");
//		first.setValue("爸爸，今后有人注册你就会收到推送信息，点击就能进入管理后台，后台密码是1314，只需登录一次以后自动登录");
//		m.put("first", first);
		//用户name字段
		TemplateData name = new TemplateData();
		name.setColor("blue");
		name.setValue(entity.getName());
		m.put("keyword1", name);
		//角色字段
		TemplateData role = new TemplateData();
		role.setColor("#FF6600");
		role.setValue(entity.getRole()==0?"求助者":"大神");
		m.put("keyword3", role);
		//手机号字段
		TemplateData phone = new TemplateData();
		phone.setColor("#666666");
		phone.setValue(entity.getPhone());
		m.put("keyword2", phone);
		//注册时间字段
		TemplateData time = new TemplateData();
		time.setColor("#666666");
		time.setValue(entity.getTime().toString().substring(0, 19));
		m.put("keyword4", time);
		t.setData(m);
		
		//获取access_token
		String param = "grant_type=client_credential&appid=wx1a4c2e86c17d1fc4&secret=ba95beea6a08dc2abf245ae8d8c7dc3e";
		String access_token_result = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", param);
		String access_token = new JSONObject(access_token_result).getString("access_token");
		System.out.println("access_token="+access_token);
		
		//发送模板消息
		String result = HttpRequest.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token,new JSONObject(t).toString());
		return true;
	}

	
	/**
	 * 获取Parameter中的ticket
	 * @return
	 */
	public String getJSTicket(){
		this.setTicket(Parameter.Ticket_Parameter);
		return "getTicket";
	}
	
	
	
	
	/**
	 * 获取Prepay_Id
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public String getPrepayId() throws ParserConfigurationException, SAXException, IOException{
		String appid = "wx1a4c2e86c17d1fc4";
		String attach = "附加数据";
		String body = "商品描述xxxxxx";
		String detail = "商品详情xxxxxxxxxxxx";
		String device_info = "WEB";
		String mch_id = "1279726201";
		String nonce_str = RandomNumber.getFixLenthString(4);
		String notify_url = "http://erhuowang.cn";
		String openid = "o6uVGv8OUlCv-OrgeK5bWBV-6i_E";
		String out_trade_no = RandomNumber.getFixLenthString(4);
//		String spbill_create_ip = "117.136.45.99";
		String spbill_create_ip = ServletActionContext.getRequest().getRemoteAddr();
		System.out.println("ip="+spbill_create_ip);
		String total_fee = "8";
		String trade_type = "JSAPI";
		
		
		String stringA = "appid="+appid+"&attach="+attach+"&body="+body+"&detail="+detail+"&device_info="
				+device_info+"&mch_id="+mch_id+"&nonce_str="+nonce_str+"&notify_url="+notify_url+"&openid="+openid+"&out_trade_no="
				+out_trade_no+"&spbill_create_ip="+spbill_create_ip+"&total_fee="+total_fee+"&trade_type="+trade_type;
		
		String stringSignTemp = stringA+"&key=chaibozhouzhouxiaobin19930620123";
		
		String sign = new MD5().GetMD5Code(stringSignTemp).toUpperCase();
		
		System.out.println("stringSignTemp="+stringSignTemp);
		System.out.println("sign="+sign);
		
		String param = "<xml>"
				+ "<appid>"+appid+"</appid>"
				+ "<attach>"+attach+"</attach>"
				+ "<body>"+body+"</body>"
				+ "<detail>"+detail+"</detail>"
				+ "<device_info>"+device_info+"</device_info>"
				+ "<mch_id>"+mch_id+"</mch_id>"
				+ "<nonce_str>"+nonce_str+"</nonce_str>"
				+ "<notify_url>"+notify_url+"</notify_url>"
				+ "<openid>"+openid+"</openid>"
				+ "<out_trade_no>"+out_trade_no+"</out_trade_no>"
				+ "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"
				+ "<total_fee>"+total_fee+"</total_fee>"
				+ "<trade_type>"+trade_type+"</trade_type>"
				+ "<sign>"+sign+"</sign>"
				+ "</xml>";
		
		System.out.println("param="+param);
		String result = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", param);
		System.out.println("result="+result);
		
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory     
                .newInstance();     
        DocumentBuilder builder = builderFactory.newDocumentBuilder();  
        Document document = builder.parse(new InputSource(new StringReader(result)));
        this.prepay_id = document.getElementsByTagName("prepay_id").item(0).getTextContent();
        System.out.println("pre_id="+prepay_id);
        
		return "getPrepayId";
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


	public String getTicket() {
		return ticket;
	}


	public void setTicket(String ticket) {
		this.ticket = ticket;
	}


	public String getPrepay_id() {
		return prepay_id;
	}


	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	
}
