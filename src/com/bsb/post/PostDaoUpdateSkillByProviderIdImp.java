package com.bsb.post;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.tools.HibernateTemplate;

/**
 * 修改指定provider的擅长的技术
 * @author chibozhou
 *
 */
public class PostDaoUpdateSkillByProviderIdImp extends HibernateTemplate {
	private String skill;
	private int provider_id;
	
	public PostDaoUpdateSkillByProviderIdImp(String skill, int provider_id) {
		super();
		this.skill = skill;
		this.provider_id = provider_id;
	}

	@Override
	protected Session handle(Session session) {
		// 更新user表
		String hql1 = "update "+Parameter.UserEntity+" set skill='"+this.skill+"' where id="+this.provider_id;
		session.createQuery(hql1).executeUpdate();
		
		// 更新need表
		String hql2 = "update "+Parameter.NeedEntity+" set provider_skill='"+this.skill+"' where provider_id="+this.provider_id;
		session.createQuery(hql2).executeUpdate();
		
		// 更新need_help表
		String hql3 = "update "+Parameter.NeedHelpEntity+" set provider_skill='"+this.skill+"' where provider_id="+this.provider_id;
		session.createQuery(hql3).executeUpdate();
		
		super.result = true;
		return session;
	}

}
