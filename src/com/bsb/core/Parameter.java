package com.bsb.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bsb.entity.UserEntity;

public class Parameter {
	public static final String AuthCodeEntity = "AuthCodeEntity";
	public static final String MsgEntity = "MsgEntity";
	public static final String NeedEntity = "NeedEntity";
	public static final String NeedHelpEntity = "NeedHelpEntity";
	public static final String UserEntity = "UserEntity";
	public static final String HibernateException = "Hibernate异常";
	public static final String AuthCodeContent1 = "【毕设帮】您的验证码为";
	public static final String AuthCodeContent2 = "(10分钟内有效)，毕业设计在线求助平台";
	public static final String SigninFailure = "用户名或密码错误";
	public static final String NoMatchProvider = "没有匹配到符合条件的大神";
	public static final String ProviderList = "providerList";
	
	public static final String IndexPage = "http://5188.help";
	public static final String LOG_FILE_PATH = "/bsb/bsb.log";
//	public static final String LOG_FILE_PATH = " /Users/chibozhou/bsb_log/bsb.log";
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	
	/** 所有大神都存在这里 */
	public static List<UserEntity> Providers_Parameters = null;
	/** access_token */
	public static String AccessToken_Parameters = null;
	/** 所有新用户的open_id和open_token都存在这里 */
	public static Map<String,String> OpenTokenId_Parameters = new HashMap<String,String>();
}
