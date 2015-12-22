package com.bsb.wechat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bsb.core.CoreDao;
import com.bsb.core.HttpRequest;
import com.bsb.core.Parameter;
import com.bsb.entity.NeedEntity;
import com.bsb.entity.NeedHelpEntity;
import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

/**
 * 所有模板消息
 * @author chibozhou
 *
 */
public class TemplateMsg {

	/**
	 * 所有模板消息的顶层抽象
	 * @param t 
	 * @return
	 */
	private static boolean sendTemplateMessage(WxTemplate t){
		
		//获取access_token
		String access_token = Parameter.AccessToken_Parameters;
		if(access_token==null || "".equals(access_token)){
			access_token = CoreDao.getAccessTokenFromWeixin();
			if(access_token==null){
				System.out.println("access_token="+access_token);
				return false;
			}
		}
		
		//发送模板消息
		String result = HttpRequest.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token,new JSONObject(t).toString());
		
		//解析返回结果
		try {
			JSONObject sendResult = new JSONObject(result);
			System.out.println(sendResult.toString());
			String errorCode = sendResult.getString("errcode");
			if(!"0".equals(errorCode))
				return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 发送有人发布信息了（管理员）
	 * @param entity
	 * @return
	 */
	public static boolean sendTemplateMsg_writeContractToAdmin(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_Admin);
		//接受者的open_id
		t.setTouser(Parameter.OpenId_Chai);
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("有求助者发布需求了，赶紧去拟定合同！");
		m.put("first", first);
		//订单编号字段
//		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
//		OrderSn.setValue("");
//		m.put("OrderSn", OrderSn);
		//订单状态字段
//		TemplateData OrderStatus = new TemplateData();
//		OrderStatus.setColor("#009900");
//		OrderStatus.setValue("交易成功");
//		m.put("OrderStatus", OrderStatus);
		//remark字段
//		TemplateData remark = new TemplateData();
//		remark.setColor("red");
//		remark.setValue("仲裁理由："+content);
//		m.put("remark", remark);
		t.setData(m);
		
		boolean result = false;
		//先发给chai
		t.setTouser(Parameter.OpenId_Chai);
		result = TemplateMsg.sendTemplateMessage(t);
		if(!result)
			return result;
		
		//再发给zhou
		t.setTouser(Parameter.OpenId_Zhou);
		result = TemplateMsg.sendTemplateMessage(t);
		return result;
	}
	
	
	/**
	 * 发送确认合同消息
	 * @param entity
	 * @return
	 */
	public static boolean sendTemplateMsg_confirmContract(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getNeeder_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DaiQianHeTong);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue(entity.getNeeder_name()+"您好，需求合同我们已经为您定制完成，请点击详情查看哦");
		m.put("first", first);
		//发送方字段
		TemplateData keyword1 = new TemplateData();
//		keyword1.setColor("blue");
		keyword1.setValue("毕设帮客户服务中心");
		m.put("keyword1", keyword1);
		//合同名称字段
		TemplateData keyword2 = new TemplateData();
//		keyword2.setColor("#FF6600");
		keyword2.setValue("毕设帮项目需求合同");
		m.put("keyword2", keyword2);
		//到期时间字段
		TemplateData keyword3 = new TemplateData();
//		keyword3.setColor("#666666");
		Date date = new Date(new Date().getTime()+2592000000l);
		keyword3.setValue(new SimpleDateFormat("yyyy-MM-dd").format(date));
		m.put("keyword3", keyword3);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("如需修改请及时与我们联系哦～");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送合同签署成功提醒
	 */
	public static boolean sendTemplateMsg_successContract(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getNeeder_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_HeTongQianShu);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue(entity.getNeeder_name()+"您好，您的需求合同已确认！等待大神抢单中");
		m.put("first", first);
		//合同编号字段
		TemplateData keyword1 = new TemplateData();
//		keyword1.setColor("blue");
		keyword1.setValue("NO:01"+entity.getId());
		m.put("keyword1", keyword1);
		//时间字段
		TemplateData keyword2 = new TemplateData();
//		keyword2.setColor("#FF6600");
		keyword2.setValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		m.put("keyword2", keyword2);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("如有疑问请随时联系我们");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送抢单通知
	 */
	public static boolean sendTemplateMsg_grabSingle(NeedHelpEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_HelperAdmin);
		//接受者的open_id
		t.setTouser(entity.getProvider_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_XinDingDan);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue(entity.getProvider_name()+"您好，"+entity.getNeeder_name()+"向您求助，请立即抢单");
		m.put("first", first);
		//提交时间字段
		TemplateData tradeDateTime = new TemplateData();
//		tradeDateTime.setColor("blue");
		tradeDateTime.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(entity.getTime().getTime())));
		m.put("tradeDateTime", tradeDateTime);
		//订单类型字段
		TemplateData orderType = new TemplateData();
//		orderType.setColor("#FF6600");
		orderType.setValue(entity.getTitle());
		m.put("orderType", orderType);
		//客户信息字段
		TemplateData customerInfo = new TemplateData();
//		customerInfo.setColor("#FF6600");
		customerInfo.setValue(entity.getNeeder_name());
		m.put("customerInfo", customerInfo);
		//价格字段
		TemplateData orderItemName = new TemplateData();
//		orderItemName.setColor("#FF6600");
		orderItemName.setValue("价格");
		m.put("orderItemName", orderItemName);
		//价格字段
		TemplateData orderItemData = new TemplateData();
//		orderItemData.setColor("#FF6600");
		orderItemData.setValue(entity.getMoney()+"元");
		m.put("orderItemData", orderItemData);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("如有疑问请随时联系我们");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送抢单成功通知to求助者
	 */
	public static boolean sendTemplateMsg_grabSingleSuccessToNe(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getNeeder_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_QiangDanChengGong);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("您的订单已被"+entity.getProvider_name()+"抢单，对方即将与您联系！");
		m.put("first", first);
		//抢单时间字段
		TemplateData keyword1 = new TemplateData();
//		keyword1.setColor("blue");
		keyword1.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		m.put("keyword1", keyword1);
		//用户姓名字段
		TemplateData keyword2 = new TemplateData();
//		keyword2.setColor("#FF6600");
		keyword2.setValue(entity.getProvider_name());
		m.put("keyword2", keyword2);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("请保持手机畅通，如有疑问请随时联系我们");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送抢单成功通知to大神
	 */
	public static boolean sendTemplateMsg_grabSingleSuccessToPro(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getProvider_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_QiangDanChengGong);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("恭喜您抢单成功！请立即与对方联系");
		m.put("first", first);
		//抢单时间字段
		TemplateData keyword1 = new TemplateData();
//		keyword1.setColor("blue");
		keyword1.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		m.put("keyword1", keyword1);
		//用户姓名字段
		TemplateData keyword2 = new TemplateData();
//		keyword2.setColor("#FF6600");
		keyword2.setValue(entity.getNeeder_name());
		m.put("keyword2", keyword2);
		//remark字段
		TemplateData remark = new TemplateData();
//		remark.setColor("red");
		remark.setValue("毕设题目："+entity.getTitle());
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送大神放弃此单通知(求助者)
	 */
	public static boolean sendTemplateMsg_proGiveupOrderToNe(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getNeeder_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("很抱歉，大神\""+entity.getProvider_name()+"\"放弃了您的订单，系统已为您重新匹配大神");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("等待大神抢单中……");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("如有疑问请随时联系我们~");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送大神放弃此单通知(大神)
	 */
	public static boolean sendTemplateMsg_proGiveupOrderToPro(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_HelperAdmin);
		//接受者的open_id
		t.setTouser(entity.getProvider_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("您已成功放弃\""+entity.getNeeder_name()+"\"的订单");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("订单已失效");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("如有疑问请随时联系我们~");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送大神确认可以开始服务(大神)
	 */
	public static boolean sendTemplateMsg_proConfirmOrderToPro(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_HelperAdmin);
		//接受者的open_id
		t.setTouser(entity.getProvider_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("您已确认订单，等待对方付款");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("等待对方付款");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("如有疑问请随时联系我们~");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送大神确认可以开始服务(求助者)
	 */
	public static boolean sendTemplateMsg_proConfirmOrderToNe(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getNeeder_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("大神已准备就绪，是否可以开始服务？");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("大神准备就绪");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("请进入帮帮后点击“开始服务”，如有疑问请随时联系我们~");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送求助者付款成功(大神)
	 */
	public static boolean sendTemplateMsg_paySuccessToPro(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_HelperAdmin);
		//接受者的open_id
		t.setTouser(entity.getProvider_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("对方已付款，请进入开发阶段");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("开发中");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("在开发过程中请与求助者多多联系，如有疑问请随时联系我们~");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送大神完成开发(大神)
	 */
	public static boolean sendTemplateMsg_finishDevToPro(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_HelperAdmin);
		//接受者的open_id
		t.setTouser(entity.getProvider_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("项目开始验收");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("验收中");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("请通过QQ远程协助为对方部署程序，如有问题随时联系我们");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送大神完成开发(求助者)
	 */
	public static boolean sendTemplateMsg_finishDevToNe(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getNeeder_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("大神已完成开发");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("验收中");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("大神将通过QQ远程协助为您部署程序，请等待大神与您联系！如有问题随时联系我们");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送求助者通过验收(求助者)
	 */
	public static boolean sendTemplateMsg_finishOrderToNe(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getNeeder_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("付款成功！资金已转入大神账户");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("交易成功");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("毕设帮感谢有你！欢迎对我们吐槽～");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送求助者通过验收(大神)
	 */
	public static boolean sendTemplateMsg_finishOrderToPro(NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_HelperAdmin);
		//接受者的open_id
		t.setTouser(entity.getProvider_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("恭喜您通过验收！资金已转入您微信账户");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("交易成功");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("毕设帮感谢有你！欢迎对我们吐槽～");
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送通知申请仲裁(管理员)
	 * @param content 仲裁的理由
	 */
	public static boolean sendTemplateMsg_applyArbitrationToAdmin(String content){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_Admin);
		//接受者的open_id
		t.setTouser(Parameter.OpenId_Chai);
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("有用户请求仲裁！");
		m.put("first", first);
		//订单编号字段
//		TemplateData OrderSn = new TemplateData();
//		OrderSn.setColor("blue");
//		OrderSn.setValue("");
//		m.put("OrderSn", OrderSn);
		//订单状态字段
//		TemplateData OrderStatus = new TemplateData();
//		OrderStatus.setColor("#009900");
//		OrderStatus.setValue("交易成功");
//		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
		remark.setColor("red");
		remark.setValue("仲裁理由："+content);
		m.put("remark", remark);
		t.setData(m);
		
		boolean result = false;
		//先发给chai
		t.setTouser(Parameter.OpenId_Chai);
		result = TemplateMsg.sendTemplateMessage(t);
		if(!result)
			return result;
		
		//再发给zhou
		t.setTouser(Parameter.OpenId_Zhou);
		result = TemplateMsg.sendTemplateMessage(t);
		return result;
	}
	
	
	/**
	 * 发送通知申请仲裁(仲裁发起者)
	 * @param content 仲裁的理由
	 */
	public static boolean sendTemplateMsg_applyArbitrationToPro(String content,String faQiZhe_openId,String faQiZhe_Role,String require_id){
		WxTemplate t = new WxTemplate();
		if("1".equals(faQiZhe_Role))
			t.setUrl(Parameter.URL_HelperAdmin);
		else
			t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(faQiZhe_openId);
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("仲裁提交成功，管理员正在处理中……");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+require_id);
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("仲裁中");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
//		remark.setColor("red");
		remark.setValue("仲裁理由:"+content);
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	
	/**
	 * 发送通知申请仲裁(被告)
	 * @param content 仲裁的理由
	 */
	public static boolean sendTemplateMsg_applyArbitrationToNe(String content,String faQiZhe_openId,String faQiZhe_Role,String require_id){
		WxTemplate t = new WxTemplate();
		if("1".equals(faQiZhe_Role))
			t.setUrl(Parameter.URL_HelperAdmin);
		else
			t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(faQiZhe_openId);
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("对方已提出仲裁，管理员正在处理中……");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+require_id);
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("仲裁中");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
//		remark.setColor("red");
		remark.setValue("仲裁理由:"+content);
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	/**
	 * 发送仲裁结束提示(求助者)
	 * @param content 仲裁的理由
	 */
	public static boolean sendTemplateMsg_finishArbitrationToNe(String content,NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_NeederAdmin);
		//接受者的open_id
		t.setTouser(entity.getNeeder_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("仲裁已完成");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("仲裁结束");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
//		remark.setColor("red");
		remark.setValue("仲裁结果:"+content);
		m.put("remark", remark);
		t.setData(m);

		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}
	
	/**
	 * 发送仲裁结束提示(大神)
	 * @param content 仲裁的理由
	 */
	public static boolean sendTemplateMsg_finishArbitrationToPro(String content,NeedEntity entity){
		WxTemplate t = new WxTemplate();
		t.setUrl(Parameter.URL_HelperAdmin);
		//接受者的open_id
		t.setTouser(entity.getProvider_weixin());
		//消息顶端颜色
		t.setTopcolor("#000000");
		//模板ID
		t.setTemplate_id(Parameter.TemplateId_DingDanZhuangTaiGengXin);
		//存储数据的Map
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		//first字段
		TemplateData first = new TemplateData();
		first.setColor("red");
		first.setValue("仲裁已完成");
		m.put("first", first);
		//订单编号字段
		TemplateData OrderSn = new TemplateData();
		OrderSn.setColor("blue");
		OrderSn.setValue("NO:01"+entity.getId());
		m.put("OrderSn", OrderSn);
		//订单状态字段
		TemplateData OrderStatus = new TemplateData();
		OrderStatus.setColor("#009900");
		OrderStatus.setValue("仲裁结束");
		m.put("OrderStatus", OrderStatus);
		//remark字段
		TemplateData remark = new TemplateData();
//		remark.setColor("red");
		remark.setValue("仲裁结果:"+content);
		m.put("remark", remark);
		t.setData(m);
		
		//发送模板消息
		return TemplateMsg.sendTemplateMessage(t);
	}

	
	
	public static void main(String[] args){
		//发送仲裁结束提示(大神)
		NeedEntity entity = new NeedEntity();
		entity.setProvider_weixin(Parameter.OpenId_Chai);
		entity.setId(25);
		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_finishArbitrationToPro("金额将全部退换求助者",entity));
		
		
		//发送仲裁结束提示(求助者)
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_finishArbitrationToNe("金额将全部退换求助者",entity));
		
		//发送通知申请仲裁(被告)
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_applyArbitrationToNe("对方质量太差了", Parameter.OpenId_Chai, "0", "33"));
		
		//发送通知申请仲裁(发起者)
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_applyArbitrationToPro("对方质量太差了", Parameter.OpenId_Chai, "0", "33"));
		
		//发送通知申请仲裁(管理员)
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_applyArbitrationToAdmin("对方联系不上了！！！"));
		
		//发送求助者通过验收(大神)
//		NeedEntity entity = new NeedEntity();
//		entity.setProvider_weixin(Parameter.OpenId_Chai);
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_finishOrderToPro(entity));
		
		//发送求助者通过验收(求助者)
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_finishOrderToNe(entity));
		
		//发送大神完成开发(求助者)
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_finishDevToNe(entity));
		
		//发送大神完成开发(大神)
//		NeedEntity entity = new NeedEntity();
//		entity.setProvider_weixin(Parameter.OpenId_Chai);
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_finishDevToPro(entity));
		
		//发送求助者付款成功(大神)
//		NeedEntity entity = new NeedEntity();
//		entity.setProvider_weixin(Parameter.OpenId_Chai);
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_paySuccessToPro(entity));
		
//		//发送大神确认可以开始服务(求助者)
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_proConfirmOrderToNe(entity));
		
		//发送大神确认可以开始服务(大神)
//		NeedEntity entity = new NeedEntity();
//		entity.setProvider_weixin(Parameter.OpenId_Chai);
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_proConfirmOrderToPro(entity));
		
		//发送大神放弃此单通知(确认订单前)(求助者)
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setProvider_name("习大大");
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_proGiveupOrderToNe(entity));
		
		//抢单成功通知to大神
//		NeedEntity entity = new NeedEntity();
//		entity.setProvider_weixin(Parameter.OpenId_Chai);
//		entity.setNeeder_name("柴博周");
//		entity.setProvider_name("习大大");
//		entity.setTitle("基于Andorid的社区服务平台");
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_grabSingleSuccessToPro(entity));
		
		//抢单成功通知to求助者
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setNeeder_name("柴博周");
//		entity.setProvider_name("习大大");
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_grabSingleSuccessToNe(entity));
		
		
		//发送确认合同消息
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setNeeder_name("柴博周");
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_confirmContract(entity));
		
		//发送合同签署成功提醒
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setNeeder_name("柴博周");
//		entity.setId(25);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_successContract(entity));
		
		//发送抢单通知
//		NeedEntity entity = new NeedEntity();
//		entity.setNeeder_weixin(Parameter.OpenId_Chai);
//		entity.setNeeder_name("柴博周");
//		entity.setProvider_name("习大大");
//		entity.setNeeder_skill("110011000111000");
//		entity.setTime(new Timestamp(new Date().getTime()));
//		entity.setTitle("基于Android的社区服务器平台");
//		entity.setMoney(520);
//		System.out.println("发送结果："+TemplateMsg.sendTemplateMsg_grabSingle(entity));

		
	}
}
