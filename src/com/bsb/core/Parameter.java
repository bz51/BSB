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
	
	public static final String OpenId_Zhou = "o6uVGv89frbuhh3qFlatwKKKzEro";
	public static final String OpenId_Chai = "o6uVGv8OUlCv-OrgeK5bWBV-6i_E";
	public static final String OpenId_Yang = "o6uVGvwNbicJU6QmFqZTzU-LlOBA";
	public static final String OpenId_Baba = "o6uVGv_lSRtbswphX0gWRZIpAvlw";
	
	
	public static final String AppId = "wx1a4c2e86c17d1fc4&secret=ba95beea6a08dc2abf245ae8d8c7dc3e";
	
	/** 后台管理的URL */
	public static final String URL_Admin = "www.erhuowang.cn/admin.html";
	/** 求助者用户中心的URL */
	public static final String URL_NeederAdmin = "www.erhuowang.cn/adminNeeder.html";
	/** 大神用户中心的URL */
	public static final String URL_HelperAdmin = "www.erhuowang.cn/adminHelper.html";
	
	/** 模板ID：会员注册成功通知 */
	public static final String TemplateId_HuiYuanZhuCe = "j7lwc2R1bALL6WrIi7Igm7OcFJMngnY6tAuW1WfK11M";
	/** 模板ID：待签合同提醒 */
	public static final String TemplateId_DaiQianHeTong = "kltsv4oggg7D48gyMdg1ruDhIj586JpKC0pKYhDjYbc";
	/** 模板ID：合同签署提醒 */
	public static final String TemplateId_HeTongQianShu = "FiaOpOj-UluHlamXX11IgBiZyeRBqCK9qHVHOI7-ack";
	/** 模板ID：新订单通知 */
	public static final String TemplateId_XinDingDan = "mts7BTs0iKAN9Um0suYcHcNdwexT6S_cCJpjojR1teQ";
	/** 模板ID：注册提醒 */
	public static final String TemplateId_ZhuCeTiXing = "pIhXQxM62druBacqoztQlv5HsixIEt2VFChpurA-mbk";
	/** 模板ID：订单状态更新 */
	public static final String TemplateId_DingDanZhuangTaiGengXin = "rhTqtWR1H0hfFI3DQAZLySeetoaz15f5G6969IhZW3Q";
	/** 模板ID：抢单成功通知 */
	public static final String TemplateId_QiangDanChengGong = "un1f9nKIW6N29csKq4cz_ewdOxmgdwfRgrNQb0KP0Jg";
	
	
	/** 所有大神都存在这里 */
	public static List<UserEntity> Providers_Parameters = null;
	/** access_token */
	public static String AccessToken_Parameters = null;
	/** ticket */
	public static String Ticket_Parameter = null;
	/** 所有新用户的open_id和open_token都存在这里 */
	public static Map<String,String> OpenTokenId_Parameters = new HashMap<String,String>();
}
