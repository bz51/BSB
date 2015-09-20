package com.bsb.post;

import java.util.List;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.NeedEntity;
import com.bsb.entity.NeedHelpEntity;
import com.bsb.tools.HibernateTemplate;

/**
 * 提高赏金
 * @author chibozhou
 *
 */
public class PostDaoIncreaseMoneyImp extends HibernateTemplate {
	private List<NeedHelpEntity> list;
	private NeedEntity needEntity;
	
	public PostDaoIncreaseMoneyImp(List<NeedHelpEntity> list, NeedEntity needEntity) {
		super();
		this.list = list;
		this.needEntity = needEntity;
	}


	@Override
	protected Session handle(Session session) {
		// 根据require_id更新need表，插入新的money和post
		String hql1 = "update "+Parameter.NeedEntity+" set money="+needEntity.getMoney()+",post=0 where id="+needEntity.getId();
		session.createQuery(hql1).executeUpdate();
		
		// 将List<NeedHelpEntity>插入need_help表
		for(NeedHelpEntity e : list){
			e.setPost(0);
			e.setNeeder_id(needEntity.getId());
			e.setMoney(needEntity.getMoney());
			e.setNeeder_name(needEntity.getNeeder_name());
			e.setNeeder_phone(needEntity.getNeeder_phone());
			e.setNeeder_skill(needEntity.getNeeder_skill());
			e.setNeeder_weixin(needEntity.getNeeder_weixin());
			e.setRequire_id(needEntity.getId());
			e.setTime(needEntity.getTime());
			e.setContent(needEntity.getContent());
			e.setTitle(needEntity.getTitle());
			session.save(e);
		}
		
		super.result = true;
		return session;
	}

}
