package com.bsb.post;

import java.util.List;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.MsgEntity;
import com.bsb.entity.NeedEntity;
import com.bsb.tools.HibernateTemplate;

/**
 * (Android)查询need表未发送的信息，并设为已发送
 * @author chibozhou
 *
 */
public class PostDaoQueryUnPushNeederMsgListImp extends HibernateTemplate {
	private List<NeedEntity> list;
	
	@Override
	protected Session handle(Session session) {
		// 查询need表state＝1，post＝0的need信息
		 String hql1 = "from "+Parameter.NeedEntity+" where state=1 and post=0";
		 list = session.createQuery(hql1).list();
		 
		// 将上述记录设置为已发送
		for(NeedEntity e : list){
			String hql2 = "update "+Parameter.NeedEntity+" set post=1 where id="+e.getId();
			session.createQuery(hql2).executeUpdate();
		}
		
		super.result = true;
		return session;
	}

	public List<NeedEntity> getList() {
		return list;
	}

	
	
}
