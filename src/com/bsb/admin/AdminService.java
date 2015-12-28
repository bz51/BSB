package com.bsb.admin;

import java.util.List;

import com.bsb.core.CoreDao;
import com.bsb.entity.FeedBackEntity;
import com.bsb.entity.UserEntity;

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
	public void doFeedback(String feedback_id) {
		this.result = CoreDao.updateByHql("update FeedBackEntity set state=0 where id="+feedback_id);
		if(!this.result)
			this.reason = "数据库更新异常！";
	}


	public boolean getResult() {
		return result;
	}


	public String getReason() {
		return reason;
	}

}
