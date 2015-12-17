package com.bsb.wechat;

import java.util.HashMap;
import java.util.Map;

import com.bsb.core.HttpRequest;
import com.qq.connect.utils.json.JSONObject;

/**
 * 微信模板消息类
 * @author chibozhou
 *
 */
public class WxTemplate {
	private String template_id;
	private String touser;
	private String url;
	private String topcolor;
	private Map<String,TemplateData> data;
	
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTopcolor() {
		return topcolor;
	}
	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}
	public Map<String,TemplateData> getData() {
		return data;
	}
	public void setData(Map<String,TemplateData> data) {
		this.data = data;
	}
	
}

