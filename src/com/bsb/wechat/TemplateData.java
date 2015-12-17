package com.bsb.wechat;

/**
 * 微信模板消息中的每一个数据项类
 * PS:每一个数据项都由valte、color构成
 * @author chibozhou
 *
 */
public class TemplateData {
	private String value;
	private String color;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}

