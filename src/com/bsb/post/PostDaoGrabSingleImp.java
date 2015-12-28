package com.bsb.post;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.NeedEntity;
import com.bsb.entity.UserEntity;
import com.bsb.tools.HibernateSessionFactory;
import com.bsb.tools.HibernateTemplate;

/**
 * provider抢单
 * @author chibozhou
 *
 */
public class PostDaoGrabSingleImp extends HibernateTemplate {
	private long require_id;
	private UserEntity providerEntity;
	private NeedEntity needEntity;
	
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
			super.reason = "手慢了！已被别人抢单";
			return session;
		}
		
		
		// 若未抢单，则将该条记录的state更新为已抢单，provider的信息填入need表
		else{
			String hql= "update "+Parameter.NeedEntity+" set state=:state,provider_weixin='"+providerEntity.getWeixin_id()+"',provider_id="+providerEntity.getId()+",provider_name='"+providerEntity.getName()+"',provider_phone='"+providerEntity.getPhone()+"',provider_skill='"+providerEntity.getSkill()+"' where id="+require_id;
//			String hql= "update "+Parameter.NeedEntity+" set state=:state,post='2',provider_weixin='"+providerEntity.getWeixin_id()+"',provider_id=31,provider_name='"+providerEntity.getName()+"',provider_phone='"+providerEntity.getPhone()+"',provider_skill='"+providerEntity.getSkill()+"' where id="+require_id;
			session.createQuery(hql).setInteger("state",1).executeUpdate();
			//特地再更新下state
//			session.createQuery("update "+Parameter.NeedEntity+" set state=1").executeUpdate();
			//将查出来的NeedEntity中加入大神信息
			entity.setProvider_id(providerEntity.getId());
			entity.setProvider_name(providerEntity.getName());
			entity.setProvider_phone(providerEntity.getPhone());
			entity.setProvider_skill(providerEntity.getSkill());
			entity.setProvider_weixin(providerEntity.getWeixin_id());
			this.needEntity = entity;
		}
		
		// 删除need_help表中所有require_id为该id的记录，提示抢单成功
		String hql = "delete "+Parameter.NeedHelpEntity+" where require_id="+this.require_id;
		session.createQuery(hql).executeUpdate();
		
		super.result = true;
		super.reason = "抢单成功";
		return session;
	}

	public NeedEntity getNeedEntity() {
		return needEntity;
	}
	
	public static void main(String[] args){
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
		
			UserEntity providerEntity = new UserEntity();
			providerEntity.setWeixin_id("weixin_id");
			providerEntity.setName("柴博周");
			providerEntity.setSkill("111111111111111");
			providerEntity.setPhone("110");
			providerEntity.setId(31);
			String require_id = "109";
			String hql= "update "+Parameter.NeedEntity+" set state=:state,provider_weixin='"+providerEntity.getWeixin_id()+"',provider_id="+providerEntity.getId()+",provider_name='"+providerEntity.getName()+"',provider_phone='"+providerEntity.getPhone()+"',provider_skill='"+providerEntity.getSkill()+"' where id="+require_id;
			session.createQuery(hql).setInteger("state",1).executeUpdate();
		
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	

}
