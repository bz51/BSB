package com.bsb.post;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.NeedEntity;
import com.bsb.tools.HibernateTemplate;

public class PostDaoApplyArbitrationImp extends HibernateTemplate {
	private String require_id;
	private String content;
	private String role;
	private NeedEntity entity;
	
	
	public PostDaoApplyArbitrationImp(String require_id, String content, String role) {
		super();
		this.require_id = require_id;
		this.content = content;
		this.role = role;
	}



	@Override
	protected Session handle(Session session) {
		//判断该条需求当前的状态，只有处于验收中的状态才能申请仲裁
		this.entity = (NeedEntity) session.createQuery("from "+Parameter.NeedEntity+" where id="+require_id).uniqueResult();
		if(entity==null){
			super.result = false;
			super.reason = "require_id不正确，未能找到该条需求";
			return session;
		}
		
		if(!"8".equals(entity.getState()+"")){
			super.result = false;
			super.reason = "该条需求不处于验收中的状态，无法申请仲裁";
			return session;
		}
		
		//修改该条记录的状态，并填写仲裁理由
		int result = session.createQuery("update "+Parameter.NeedEntity+" set state=10,zhongcai=:zhongcaiString where id=:idString")
				.setString("zhongcaiString", content)
				.setString("idString", require_id)
				.executeUpdate();
		if(result<=0){
			super.result = false;
			super.reason = "更新状态发生异常，请重试!";
			return session;
		}
		
		return session;
	}



	public NeedEntity getEntity() {
		return entity;
	}

}
