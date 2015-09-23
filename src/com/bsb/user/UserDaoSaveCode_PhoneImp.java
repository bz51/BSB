package com.bsb.user;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.AuthCodeEntity;
import com.bsb.tools.HibernateTemplate;

public class UserDaoSaveCode_PhoneImp extends HibernateTemplate {
	private AuthCodeEntity entity;
	
	public UserDaoSaveCode_PhoneImp(AuthCodeEntity entity) {
		super();
		this.entity = entity;
	}

	@Override
	protected Session handle(Session session) {
		//判断手机号是否存在
		long count = (long) session.createQuery("select count(id) from "+Parameter.UserEntity+" where phone=:phoneString")
				.setString("phoneString", entity.getPhone())
				.uniqueResult();
		
		System.out.println("count:"+count);
		
		//若不存在则保存验证信息
		if(count==0){
			entity.setPost(0);
			session.save(entity);
		}
		
		//若存在就返回错误提示
		else{
			super.result = false;
			super.reason = "手机号:"+entity.getPhone()+"已存在！";
		}
		
		return session;
	}
	

}
