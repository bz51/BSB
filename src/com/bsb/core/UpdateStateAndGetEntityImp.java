package com.bsb.core;

import org.hibernate.Session;

import com.bsb.entity.NeedEntity;
import com.bsb.tools.HibernateTemplate;

/**
 * 本类用于更新NeedEntity表的状态，并返回更新后的整条记录
 * @author chibozhou
 *
 */
public class UpdateStateAndGetEntityImp extends HibernateTemplate {
	private NeedEntity needEntity;
	/** 新的状态 */
	private int state;
	/** 该条需求的ID */
	private long require_id;
	

	public UpdateStateAndGetEntityImp(int state, long require_id) {
		super();
		this.state = state;
		this.require_id = require_id;
	}


	@Override
	protected Session handle(Session session) {
		//更新状态
		String hql = "update NeedEntity set state=:state where id=:idString";
		session.createQuery(hql)
			.setLong("idString", require_id)
			.setInteger("state", state)
			.executeUpdate();
		
		//获取该条记录
		String hql2 = "from NeedEntity where id=:idString";
		this.needEntity = (NeedEntity) session.createQuery(hql2)
			.setLong("idString", require_id)
			.uniqueResult();
		return session;
	}


	public NeedEntity getNeedEntity() {
		return needEntity;
	}
	
	public static void main(String[] args){
		UpdateStateAndGetEntityImp imp = new UpdateStateAndGetEntityImp(2,97);
		System.out.println("result="+imp.hibernateOperation());
		System.out.println("id="+imp.getNeedEntity().getId());
		System.out.println("content="+imp.getNeedEntity().getContent());
		System.out.println("needer_id="+imp.getNeedEntity().getNeeder_id());
		System.out.println("id="+imp.getNeedEntity().getNeeder_name());
		System.out.println("id="+imp.getNeedEntity().getNeeder_phone());
		System.out.println("id="+imp.getNeedEntity().getNeeder_skill());
	}

}
