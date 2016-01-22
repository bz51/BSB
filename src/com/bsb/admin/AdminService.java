package com.bsb.admin;

import java.util.List;

import com.bsb.core.CoreDao;
import com.bsb.core.Parameter;
import com.bsb.entity.FeedBackEntity;
import com.bsb.entity.NeedEntity;
import com.bsb.entity.UserEntity;
import com.bsb.wechat.TemplateMsg;

public class AdminService {
	private boolean result = true;
	private String reason;
	
	/**
	 * 获取所有用户的信息
	 * @return
	 */
	public List<UserEntity> getAllUser() {
		return CoreDao.queryListByHql("from UserEntity order by time desc");
	}

	
	/**
	 * 获取所有反馈信息
	 * @return
	 */
	public List<FeedBackEntity> getFeedback() {
		return CoreDao.queryListByHql("from FeedBackEntity where state=1 order by time desc");
	}


	/**
	 * 处理一条反馈信息
	 */
	public void doFeedback(String feedback_id,String require_id) {
		this.result = CoreDao.updateByHql("update FeedBackEntity set state=0 where id="+feedback_id);
		if(!this.result){
			this.reason = "数据库更新异常！";
			return;
		}
		
		//处理成功，则向求助者发一条推送信息
		else{
			//根据require_id查open_id
			NeedEntity needEntity = CoreDao.queryUniqueById(Long.parseLong(require_id), Parameter.NeedEntity);
			if(needEntity==null){
				this.result = false;
				this.reason = "根据require_id查NeedEntity时发生异常！";
				return;
			}
			//发送推送消息
			TemplateMsg.sendTemplateMsg_adminModifyContractToNe(needEntity);
		}
	}


	public boolean getResult() {
		return result;
	}


	public String getReason() {
		return reason;
	}

}
