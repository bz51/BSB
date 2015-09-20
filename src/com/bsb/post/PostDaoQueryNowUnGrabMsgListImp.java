package com.bsb.post;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.AuthCodeEntity;
import com.bsb.entity.NeedHelpEntity;
import com.bsb.tools.HibernateTemplate;

/**
 * (Android)查询need_help中所有未发送的短信,并设为已发送
 * @author chibozhou
 *
 */
public class PostDaoQueryNowUnGrabMsgListImp extends HibernateTemplate {
	private List<NeedHelpEntity> list;
	
	@Override
	protected Session handle(Session session) {
		// 查询need_help中所有未发送的短信
		String hql1 = "from "+Parameter.NeedHelpEntity+" where post=0";
		list = session.createQuery(hql1).list();
		
		// 将所有未发送的信息设为已发送
		for(NeedHelpEntity e : list){
			String hql2 = "update "+Parameter.NeedHelpEntity+" set post=1 where id="+e.getId();
			session.createQuery(hql2).executeUpdate();
		}
		
		super.result = true;
		return session;
	}

	public List<NeedHelpEntity> getList() {
		return list;
	}
	
	

}
