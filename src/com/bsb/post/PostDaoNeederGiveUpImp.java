package com.bsb.post;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.tools.HibernateTemplate;

/**
 * needer放弃一个订单
 * @author chibozhou
 *
 */
public class PostDaoNeederGiveUpImp extends HibernateTemplate {
	private int require_id;

	public PostDaoNeederGiveUpImp(int require_id) {
		super();
		this.require_id = require_id;
	}
	
	
	@Override
	protected Session handle(Session session) {
		//判断该条需求是否已被抢单
		String hql = "select state from "+Parameter.NeedEntity+" where id="+require_id;
		int state = (int) session.createQuery(hql).uniqueResult();
		
		//若未被抢单，则可以放弃该单
		if(state==0 || state==3){
			// 更新need表，将state设为无效
			String hql1 = "update "+Parameter.NeedEntity+" set state=2 where id="+this.require_id;
			session.createQuery(hql1).executeUpdate();
		
			// 删除need_help表中所有的require_id
			String hql2 = "delete "+Parameter.NeedHelpEntity+" where require_id="+this.require_id;
			session.createQuery(hql2).executeUpdate();
		}
		
		//若已被抢单，则无法放弃该单
		else{
			super.result = false;
			super.reason = "订单状态已更新！即将刷新";
		}
		
		return session;
	}
	

}
