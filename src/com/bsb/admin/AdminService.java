package com.bsb.admin;

import java.util.List;

import com.bsb.core.CoreDao;
import com.bsb.entity.UserEntity;

public class AdminService {

	/**
	 * 获取所有用户的信息
	 * @return
	 */
	public List<UserEntity> getAllUser() {
		return CoreDao.queryListByHql("from UserEntity order by time desc");
	}

}
