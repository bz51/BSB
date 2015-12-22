package com.bsb.post;

import org.hibernate.Session;

import com.bsb.entity.NeedEntity;
import com.bsb.tools.HibernateTemplate;

public class PostDaoPostContract extends HibernateTemplate {
	private String contract;
	private String require_id;
	private NeedEntity needEntity;
	
	public PostDaoPostContract(String contract, String require_id) {
		super();
		this.contract = contract;
		this.require_id = require_id;
	}


	@Override
	protected Session handle(Session session) {
		//更新合同和状态
		String hql = "update NeedEntity set state=4 , contract=:contract where id=:idString";
		session.createQuery(hql)
			.setString("idString", require_id)
			.setString("contract", contract)
			.executeUpdate();
		
		//获取该条记录
		String hql2 = "from NeedEntity where id=:idString";
		this.needEntity = (NeedEntity) session.createQuery(hql2)
			.setString("idString", require_id)
			.uniqueResult();
		return session;
	}


	public NeedEntity getNeedEntity() {
		return needEntity;
	}
	
	

}
