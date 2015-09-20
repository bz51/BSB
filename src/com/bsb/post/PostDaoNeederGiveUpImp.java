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
		// 更新need表，将state设为无效
		String hql1 = "update "+Parameter.NeedEntity+" set state=2 where id="+this.require_id;
		session.createQuery(hql1).executeUpdate();
		
		// 删除need_help表中所有的require_id
		String hql2 = "from "+Parameter.NeedHelpEntity+" where require_id="+this.require_id;
		session.delete(hql2);
		
		return session;
	}
	

}
