package com.bsb.user;

import java.util.List;

import org.junit.Test;

import com.bsb.entity.AuthCodeEntity;
import com.bsb.tools.HibernateTemplate;

public class UserDaoQueryMsgListImpTest {

	@Test
	public void testQueryMsgList() {
		UserDaoQueryMsgListImp imp = new UserDaoQueryMsgListImp();
		boolean result = imp.hibernateOperation();
		System.out.println("执行结果："+result);
		if(result){
			List<AuthCodeEntity> list = imp.getList();
			System.out.println("未发送短信条数："+list.size());
			for(AuthCodeEntity e : list){
				System.out.println("id:"+e.getId());
				System.out.println("code:"+e.getCode());
				System.out.println("phone:"+e.getPhone());
				System.out.println("post:"+e.getPost());
			}
		}
	}

}
