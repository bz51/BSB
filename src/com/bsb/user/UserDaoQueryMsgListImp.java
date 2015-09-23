package com.bsb.user;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.AuthCodeEntity;
import com.bsb.tools.HibernateTemplate;

/**
 * (Android)查询所有未发送的短信,并设为已发送
 * @author chibozhou
 *
 */
public class UserDaoQueryMsgListImp extends HibernateTemplate{
	private List<AuthCodeEntity> list;
	
	@Override
	protected Session handle(Session session) {
		//查询所有未发送的信息
		String hql1 = "from "+Parameter.AuthCodeEntity+" where post=0";
		this.list = session.createQuery(hql1).list(); 
		
		//将这些信息设为已发送(删掉这些记录)
		for(AuthCodeEntity e : list){
			String hql2 = "delete "+Parameter.AuthCodeEntity+" where id="+e.getId();
			session.createQuery(hql2).executeUpdate();
		}
		
		super.result = true;
		return session;
	}

	public List<AuthCodeEntity> getList() {
		return list;
	}
	
	

}
