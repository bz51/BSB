package com.bsb.post;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.bsb.entity.NeedEntity;
import com.bsb.entity.NeedHelpEntity;
import com.bsb.tools.HibernateTemplate;

/**
 * 保存一个需求,并将匹配结果存入need_help表
 * @author chibozhou
 *
 */
public class PostDaoPostNeedImp extends HibernateTemplate {
	private NeedEntity needEntity;/** 需求的具体内容 */
	private List<NeedHelpEntity> needHelpList;/** Action匹配到大神的信息 */
	
	
	public PostDaoPostNeedImp(NeedEntity needEntity, List<NeedHelpEntity> needHelpList) {
		super();
		this.needEntity = needEntity;
		this.needHelpList = needHelpList;
	}



	@Override
	protected Session handle(Session session) {
		// 将需求和needer信息填入need表中，并返回require_id
		needEntity.setPost(0);
		needEntity.setState(0);
		needEntity.setTime(new Timestamp(new Date().getTime()));
		int require_id = (int) session.save(needEntity);
		
		// 将needer信息、需求信息、require_id 、provider信息组合在一起，填入need_help表中
		for(NeedHelpEntity e : needHelpList){
			e.setPost(0);
			e.setNeeder_id(needEntity.getId());
			e.setMoney(needEntity.getMoney());
			e.setNeeder_name(needEntity.getNeeder_name());
			e.setNeeder_phone(needEntity.getNeeder_phone());
			e.setNeeder_skill(needEntity.getNeeder_skill());
			e.setNeeder_weixin(needEntity.getNeeder_weixin());
			e.setRequire_id(require_id);
			e.setTime(needEntity.getTime());
			e.setContent(needEntity.getContent());
			e.setTitle(needEntity.getTitle());
			session.save(e);
		}
		
		super.result = true;
		return session;
	}

}
