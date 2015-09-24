package com.bsb.post;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.tools.HibernateTemplate;

public class PostDaoIncreaseMoneyImp_New extends HibernateTemplate{
	private int require_id;
	private int money;
	private int count;
	
	public PostDaoIncreaseMoneyImp_New(int require_id, int money) {
		super();
		this.require_id = require_id;
		this.money = money;
	}


	@Override
	protected Session handle(Session session) {
		// 根据require_id更新need表，插入新的money
		String hql1 = "update "+Parameter.NeedEntity+" set money="+money+" where id="+require_id;
		session.createQuery(hql1).executeUpdate();
		
		// 根据require_id更新need_help表，更新money和post
		String hql2 = "update "+Parameter.NeedHelpEntity+" set money="+money+",post=0 where require_id="+require_id;
		this.count = session.createQuery(hql2).executeUpdate();
		return session;
	}


	public int getCount() {
		return count;
	}
	
	

}
