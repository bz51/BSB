package com.bsb.post;

import org.hibernate.Session;

import com.bsb.entity.NeedEntity;
import com.bsb.tools.HibernateTemplate;

public class PostDaoConfirmOrderImp extends HibernateTemplate {
	private String needer_id;
	private String password;
	private String require_id;
	private NeedEntity needEntity;

	public PostDaoConfirmOrderImp(String require_id, String password, String needer_id) {
		super();
		this.needer_id = needer_id;
		this.password = password;
		this.require_id = require_id;
	}
	
	
	
	@Override
	protected Session handle(Session session) {
		//获取该条记录
		String hql2 = "from NeedEntity where id=:idString";
		this.needEntity = (NeedEntity) session.createQuery(hql2)
			.setString("idString", require_id)
			.uniqueResult();
		
		//判断该记录的状态是否为验收中
		if(this.needEntity==null){
			super.result = false;
			super.reason = "require_id不存在";
			return session;
		}
		
		
		// 若已抢单则返回错误提示；
		if(this.needEntity.getState()!=8){
			super.result = false;
			super.reason = "当前页面已失效!";
			return session;
		}
		
		//验证密码是否正确
		int size = (int) session.createQuery("select id from UserEntity where id="+this.needer_id+" and password='"+this.password+"'")
			.list().size();
		System.out.println("size="+size);
		if(size!=1){
			super.result = false;
			super.reason = "密码不正确";
			return session;
		}
		
		//把钱打给大神
		//……&……&………………………………………………
		
		String hql = "update NeedEntity set state=9 where id=:idString";
		session.createQuery(hql)
			.setString("idString", require_id)
			.executeUpdate();
		
		return session;
	}

	public NeedEntity getNeedEntity() {
		return needEntity;
	}
	
}
