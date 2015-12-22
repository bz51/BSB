package com.bsb.post;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.bsb.entity.NeedEntity;
import com.bsb.entity.NeedHelpEntity;
import com.bsb.tools.HibernateTemplate;

public class PostDaoMatchProviderImp extends HibernateTemplate {
	private String require_id;
	private List<NeedHelpEntity> list;
	private NeedEntity needEntity;

	public PostDaoMatchProviderImp(String require_id, List<NeedHelpEntity> list) {
		super();
		this.require_id = require_id;
		this.list = list;
	}


	@Override
	protected Session handle(Session session) {
		//更新need表state为0
		String hql = "update NeedEntity set state=0 where id=:idString";
		session.createQuery(hql)
			.setString("idString", require_id)
			.executeUpdate();
		
		//查询need表中该条需求的全部信息
		String hql2 = "from NeedEntity where id=:idString";
		System.out.println("!!!!id="+require_id);
		this.needEntity = (NeedEntity) session.createQuery(hql2)
			.setString("idString", require_id)
			.uniqueResult();
		
		//将needHelpEntity存入DB
		for(NeedHelpEntity e : list){
			e.setPost(0);
			e.setNeeder_id(needEntity.getId());
			e.setMoney(needEntity.getMoney());
			e.setNeeder_name(needEntity.getNeeder_name());
			e.setNeeder_phone(needEntity.getNeeder_phone());
			e.setNeeder_skill(needEntity.getNeeder_skill());
			e.setNeeder_weixin(needEntity.getNeeder_weixin());
			e.setRequire_id(Integer.parseInt(require_id));
			e.setTime(needEntity.getTime());
			e.setContent(needEntity.getContent());
			e.setTitle(needEntity.getTitle());
			e.setContract(needEntity.getContract());
			session.save(e);
		}
		
		return session;
	}
	
	
	public NeedEntity getNeedEntity() {
		return needEntity;
	}
}
