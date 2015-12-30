package com.bsb.wechat;

import com.bsb.core.CoreDao;
import com.bsb.core.Parameter;
import com.bsb.entity.NeedEntity;

public class WeChatService {
	private boolean result = true;
	private String reason;
	
	
	public boolean getResult() {
		return result;
	}
	public String getReason() {
		return reason;
	}
	
	
	/**
	 * 获取Prepay_Id
	 * @param require_id
	 * @return
	 */
	public NeedEntity getPrepayId(String require_id) {
		NeedEntity entity = CoreDao.queryUniqueById(Long.parseLong(require_id), Parameter.NeedEntity);
		//查询失败
		if(entity==null){
			this.result = false;
			this.reason = "数据库查询异常！";
			return entity;
		}
		
		//判断订单当前状态，只有当状态为等待付款才能付款
		if(entity.getState()!=6){
			this.result = false;
			this.reason = "当前订单状态为"+entity.getState()+",仅当状态为6时才能付款！";
			return entity;
		}
		
		//判断entity中money、needer_weixin、title是否都是完整的
		if(entity.getMoney()<=0){
			this.result = false;
			this.reason = "本订单的金额小于等于0！money="+entity.getMoney();
			return entity;
		}
		if(entity.getNeeder_weixin()==null || "".equals(entity.getNeeder_weixin())){
			this.result = false;
			this.reason = "求助者的微信ID非法！open_id="+entity.getNeeder_weixin();
			return entity;
		}
		if(entity.getTitle()==null || "".equals(entity.getTitle())){
			this.result = false;
			this.reason = "本订单的标题为空！title="+entity.getTitle();
			return entity;
		}
		
		//将金额转换成“分”(在付款的时候转！！！)
//		entity.setMoney(Integer.parseInt(entity.getMoney()+"00"));
		return entity;
	}
	
	
}
