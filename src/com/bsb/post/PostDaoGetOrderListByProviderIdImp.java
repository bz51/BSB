package com.bsb.post;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.bsb.core.Parameter;
import com.bsb.entity.NeedEntity;
import com.bsb.entity.NeedHelpEntity;
import com.bsb.tools.HibernateTemplate;

/**
 * 获取指定provider的所有订单情况
 * @author chibozhou
 *
 */
public class PostDaoGetOrderListByProviderIdImp extends HibernateTemplate {
	private int provider_id;
	private List<NeedEntity> list = new ArrayList<NeedEntity>();
	
	public PostDaoGetOrderListByProviderIdImp(int provider_id) {
		super();
		this.provider_id = provider_id;
	}

	@Override
	protected Session handle(Session session) {
		// 从need_help表中获取该provider所有等待抢单的订单
		String hql1 = "from "+Parameter.NeedHelpEntity+" where provider_id="+this.provider_id+" order by time desc";
		List<NeedHelpEntity> needHelpList = session.createQuery(hql1).list();
		// 将所有未抢单订单放入list中
		for(NeedHelpEntity e : needHelpList){
			NeedEntity entity = new NeedEntity();
			entity.setId(e.getRequire_id());
			entity.setNeeder_id(e.getNeeder_id());
			entity.setNeeder_name(e.getNeeder_name());
			entity.setNeeder_phone(e.getNeeder_phone());
			entity.setNeeder_skill(e.getNeeder_skill());
			entity.setNeeder_weixin(e.getNeeder_weixin());
			entity.setContent(e.getContent());
			entity.setMoney(e.getMoney());
			entity.setTime(e.getTime());
			entity.setTitle(e.getTitle());
			entity.setContract(e.getContract());
			list.add(entity);
		}
		
		// 从need表中获取该provider所有抢单成功的订单
		String hql2 = "from "+Parameter.NeedEntity+" where provider_id="+this.provider_id;
//		String hql2 = "from "+Parameter.NeedEntity+" where provider_id="+this.provider_id+" and state=1";
		List<NeedEntity> needList = session.createQuery(hql2).list();
		// 将成功抢到的单填入list中
		for(NeedEntity e : needList)
			list.add(e);
		
		super.result = true;
		return session;
	}

	public List<NeedEntity> getList() {
		return list;
	}

	
}
