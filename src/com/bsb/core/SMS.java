package com.bsb.core;

import com.qq.connect.utils.json.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SMS {
	private static final String url = "http://gw.api.taobao.com/router/rest";
	private static final String appkey = "23258617";
	private static final String secret = "cbcdff4efa24f90f6ebc7eaddd0a0184";
	
	public static boolean sendMsg(String code,String phone)
	{
		if(code==null || "".equals(code) || phone==null || "".equals(phone))
			return false;
		
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName("注册验证");
		req.setSmsParam("{\"code\":\""+code+"\",\"product\":\"【毕设帮】\"}");
		req.setRecNum(phone);
		req.setSmsTemplateCode("SMS_1615033");
		AlibabaAliqinFcSmsNumSendResponse response;
		try {
			response = client.execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println(response.getBody());
		
		return true;
//		JSONObject json = new JSONObject(response.getBody());
//		json.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getString("result");
	}
	
//	public void aaa(){
//		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
//		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//		req.setExtend("123456");
//		req.setSmsType("normal");
//		req.setSmsFreeSignName("签名");
//		req.setSmsParam("{\"AckNum\":\"123456\"}");
//		req.setRecNum("13000000000");
//		req.setSmsTemplateCode("SMS_100001");
//		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req, sessionKey);
//		System.out.println(rsp.getBody());
//	}
}
