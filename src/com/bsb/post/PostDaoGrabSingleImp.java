package com.bsb.post;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.NeedEntity;
import com.bsb.entity.UserEntity;
import com.bsb.tools.HibernateTemplate;

/**
 * provider抢单
 * @author chibozhou
 *
 */
public class PostDaoGrabSingleImp extends HibernateTemplate {
	private long require_id;
	private UserEntity providerEntity;
	
	public PostDaoGrabSingleImp(long require_id, UserEntity providerEntity) {
		super();
		this.require_id = require_id;
		this.providerEntity = providerEntity;
	}

	@Override
	protected Session handle(Session session) {
		// 获取指定require_id的需求
		String hql1 = "from "+Parameter.NeedEntity+" where id="+this.require_id;
		NeedEntity entity = (NeedEntity) session.createQuery(hql1).uniqueResult();
		
		if(entity==null){
			super.result = false;
			super.reason = "require_id不存在";
			return session;
		}
		
		
		// 若已抢单则返回错误提示；
		if(entity.getState()!=0){
			super.result = false;
			super.reason = "已被抢单";
			return session;
		}
		
		// 若未抢单，则将该条记录的state更新为已抢单，provider的信息填入need表
		else{
			String hql= "update "+Parameter.NeedEntity+" set provider_weixin='"+providerEntity.getWeixin_id()+"',provider_id="+providerEntity.getId()+",provider_name='"+providerEntity.getName()+"',provider_phone='"+providerEntity.getPhone()+"',provider_skill='"+providerEntity.getSkill()+"',state=1";
			session.createQuery(hql).executeUpdate();
		}
		
		// 删除need_help表中所有require_id为该id的记录，提示抢单成功
		String hql = "from "+Parameter.NeedHelpEntity+" where require_id="+this.require_id;
		session.delete(hql);
		
		super.result = true;
		super.reason = "抢单成功";
		return session;
	}

}
