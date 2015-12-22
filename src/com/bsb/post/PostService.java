package com.bsb.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bsb.core.CoreDao;
import com.bsb.core.Parameter;
import com.bsb.core.UpdateStateAndGetEntityImp;
import com.bsb.entity.MsgEntity;
import com.bsb.entity.NeedEntity;
import com.bsb.entity.NeedHelpEntity;
import com.bsb.entity.UserEntity;
import com.bsb.wechat.TemplateMsg;

public class PostService {
	private boolean result = true;
	private String reason;
	
	/**
	 * 获取当前所有入驻的provider(每隔1小时调用一次)
	 * @return
	 */
	public List<UserEntity> getAllProviderList(){
		//获取所有provider
		String hql = "from "+Parameter.UserEntity+" where role=1 and state=1";
		return CoreDao.queryListByHql(hql);
	}
	
	
	/**
	 * 发布一个需求(弃用)
	 * @return
	 */
	/**
	public boolean postNeed(NeedEntity needEntity,List<NeedHelpEntity> needHelpList){
		//健壮性判断
		if(needEntity==null || needEntity.getContent()==null || needEntity.getContent().equals("")
				|| needEntity.getMoney()==0 || needEntity.getNeeder_id()==0
				|| needEntity.getNeeder_name()==null || needEntity.getNeeder_name().equals("")
				|| needEntity.getNeeder_phone()==null || needEntity.getNeeder_phone().equals("")
				|| needEntity.getNeeder_skill()==null || needEntity.getNeeder_skill().equals("")
				|| needEntity.getTitle()==null || needEntity.getTitle().equals("")){
			this.result = false;
			this.reason = "发布者的name、phone、skill不能为空，需求的title、content、money不能为空";
			return false;
		}
		
		if(needHelpList==null || needHelpList.size()==0){
			this.result = false;
			this.reason = Parameter.NoMatchProvider;//没有匹配到符合条件的大神
			return false;
		}
		
		//发布需求
		PostDaoPostNeedImp imp = new PostDaoPostNeedImp(needEntity, needHelpList);
		return imp.hibernateOperation();
		
	}
	*/
	
	/**
	 * 发布一条需求（现用）
	 * PS:健壮性判断放到Action层完成，这里不再做判断了！
	 */
	public void postNeed(NeedEntity needEntity){
		//将状态设置为“3:拟定合同中”
		needEntity.setState(3);
		
		//将信息存入need表
		int result = CoreDao.save(needEntity);
		if(result==-1){
			this.result = false;
			this.reason = "数据库插入失败";
			return;
		}
		
		//通知管理员拟定合同
		TemplateMsg.sendTemplateMsg_writeContractToAdmin(needEntity);
	}
	
	
	/**
	 * (Android)查询need_help中所有未发送的短信,并设为已发送
	 * @return
	 */
	public List<MsgEntity> queryNowUnGrabMsgList(){
		//获取短信
		PostDaoQueryNowUnGrabMsgListImp imp = new PostDaoQueryNowUnGrabMsgListImp();
		this.result = imp.hibernateOperation();
		this.reason = imp.getReason();
		
		//将短信装入MsgEntity
		List<MsgEntity> msgList = new ArrayList<MsgEntity>();
		for(NeedHelpEntity e : imp.getList()){
			MsgEntity msgEntity = new MsgEntity();
			msgEntity.setPhone(e.getProvider_phone());
			msgEntity.setContent("[毕设帮]您有新订单！"+e.getNeeder_name()+"向您求助，点击链接立即抢单:"+Parameter.IndexPage);
			msgList.add(msgEntity);
		}
		return msgList;
	}
	
	
	/**
	 * 根据require_id获取该条需求的详情
	 * @param require_id
	 * @return
	 */
	public NeedEntity getNeedDetail(int require_id){
		//健壮性判断
		if(require_id<=0){
			this.result = false;
			this.reason = "require_id不能<=0！";
			return null;
		}
		
		//获取该条需求的详情
		return CoreDao.queryUniqueById(require_id, Parameter.NeedEntity);
	}
	
	
	/**
	 * provider抢单
	 * @param require_id
	 * @param providerEntity
	 * @return
	 */
	public boolean grabSingle(int require_id,UserEntity providerEntity){
		//健壮性判断
		if(require_id<=0 || providerEntity==null || providerEntity.getId()<=0 || providerEntity.getName()==null
				|| providerEntity.getName().equals("") || providerEntity.getPhone()==null || providerEntity.getPhone().equals("")
				|| providerEntity.getRole()<=0 || providerEntity.getSkill()==null || providerEntity.getSkill().equals("")){
			this.result = false;
			this.reason = "require_id不能<=0，provider的id、name、phone、role、skill不能为空";
			return false;
		}
		
		//进行抢单
		PostDaoGrabSingleImp imp = new PostDaoGrabSingleImp(require_id, providerEntity);
		boolean result = imp.hibernateOperation();
		if(!result)
			this.reason = imp.getReason();
		
		//向求助者发送抢单成功通知
		TemplateMsg.sendTemplateMsg_grabSingleSuccessToNe(imp.getNeedEntity());
		//向大神发送抢单成功通知
		TemplateMsg.sendTemplateMsg_grabSingleSuccessToPro(imp.getNeedEntity());
		return result;
	}
	
	
	/**
	 * (Android)查询need表中30分钟未抢单的需求
	 * 时间超过30分钟的记录
	 * 步骤：查询所有已发送＋未被抢单的记录，再筛选出时间超过30分钟+小于60分钟的记录
	 * @return
	 */
	public List<MsgEntity> query30MUnGrabMsgList(){
		// 获取短信
		String hql = "from "+Parameter.NeedEntity+" where state=0 and post=1";
		List<NeedEntity> needList = CoreDao.queryListByHql(hql);
		
		//用于存放筛选后的NeedEntity
		List<NeedEntity> needList_timeThan30 = new ArrayList<NeedEntity>();
		
		// 筛选超过30分钟的记录
		if (needList != null) {
			for(int i=0;i<needList.size();i++){
				//获取当前记录的时间
				Date time = new Date(needList.get(i).getTime().getTime());
				//若大于30分钟，则加入needList_timeThan30
				if( (new Date().getTime()-time.getTime())>=1800000 && (new Date().getTime()-time.getTime())<3600000)
					needList_timeThan30.add(needList.get(i));
			}
		}
		
			// 将短信装入MsgEntity
			List<MsgEntity> msgList = new ArrayList<MsgEntity>();
			for (NeedEntity e : needList_timeThan30) {
				MsgEntity msgEntity = new MsgEntity();
				msgEntity.setPhone(e.getProvider_phone());
				msgEntity.setContent("[毕设帮]您的订单30分钟未被抢单，是否提升赏金？查看订单请点下面链接:" + Parameter.IndexPage);
				msgList.add(msgEntity);
			}
			return msgList;
	}
	


	/**
	 * 提高赏金（现用）
	 * @param id
	 * @param money
	 * @return
	 */
	public int increaseMoney_new(int id, int money) {
		//提高赏金
		PostDaoIncreaseMoneyImp_New imp = new PostDaoIncreaseMoneyImp_New(id, money);
		boolean result = imp.hibernateOperation();
		if(result)
			return imp.getCount();
		else{
			this.result = imp.getResult();
			this.reason = imp.getReason();
			return -1;
		} 
	}
	
	/**
	 * 提高赏金
	 * @param needEntity
	 * @param needHelpList
	 * @return
	 */
	public boolean increaseMoney(NeedEntity needEntity,List<NeedHelpEntity> needHelpList){
		//健壮性判断
		if(needEntity==null || needEntity.getContent()==null || needEntity.getContent().equals("")
				|| needEntity.getId()<=0 || needEntity.getMoney()<=0 || needEntity.getNeeder_id()<=0
				|| needEntity.getNeeder_name()==null || needEntity.getNeeder_name().equals("")
				|| needEntity.getNeeder_phone()==null || needEntity.getNeeder_phone().equals("")
				|| needEntity.getNeeder_skill()==null || needEntity.getNeeder_skill().equals("")
				|| needEntity.getTitle()==null || needEntity.getTitle().equals("")){
			this.result = false;
			this.reason = "needEntity、需求的content、title、money不能为空、求助者的id、name、phone、skill不能为空";
			return false;
		}
		
		//提高赏金
		PostDaoIncreaseMoneyImp imp = new PostDaoIncreaseMoneyImp(needHelpList, needEntity);
		return imp.hibernateOperation();
	}
	
	
	/**
	 * 求助者放弃订单
	 * @param require_id
	 * @return
	 */
	public boolean neederGiveUp(int require_id){
		//健壮性判断
		if(require_id<=0){
			this.result = false;
			this.reason = "require_id不能<=0";
			return false;
		}
		
		//放弃一个订单
		PostDaoNeederGiveUpImp imp = new PostDaoNeederGiveUpImp(require_id);
		boolean result = imp.hibernateOperation();
		if(result)
			return true;
		else{
			this.result = imp.getResult();
			this.reason = imp.getReason();
			return false;
		}
	}
	
	
	/**
	 * 获取指定needer的所有订单情况
	 * @param needer_id
	 * @return
	 */
	public List<NeedEntity> getOrderListByNeederId(int needer_id){
		// 健壮性判断
		if (needer_id <= 0) {
			this.result = false;
			this.reason = "needer_id不能<=0";
			return null;
		}
		
		// 获取
		String hql = "from "+Parameter.NeedEntity+" where needer_id="+needer_id +"order by time desc";
		
		// 对结果进行排序：被抢单排在前、未被抢单排在后、失效单排最后＋相同情况下根据时间排序
		List<NeedEntity> list_0 = new ArrayList<NeedEntity>();//专门存放state为0的记录
		List<NeedEntity> list_1 = new ArrayList<NeedEntity>();//专门存放state为1的记录
		List<NeedEntity> list_2 = new ArrayList<NeedEntity>();//专门存放state为2的记录
		// 所有的记录
		List<NeedEntity> list_all = CoreDao.queryListByHql(hql);
		for(NeedEntity e : list_all){
			if(e.getState()==0)
				list_0.add(e);
			else if(e.getState()==1)
				list_1.add(e);
			else
				list_2.add(e);
		}
		
		// 将存放所有记录的list_all清空，然后把三个list按照顺序存进去
		list_all.clear();
		list_all.addAll(list_1);
		list_all.addAll(list_0);
		list_all.addAll(list_2);
		
		return list_all;
	}
	
	
	/**
	 * 获取指定provider的所有订单情况
	 * @param provider_id
	 * @return
	 */
	public List<NeedEntity> getOrderListByProviderId(int provider_id){
		// 健壮性判断
		if (provider_id <= 0) {
			this.result = false;
			this.reason = "provider_id不能<=0";
			return null;
		}
		
		// 获取
		PostDaoGetOrderListByProviderIdImp imp = new PostDaoGetOrderListByProviderIdImp(provider_id);
		this.result = imp.hibernateOperation();
		
		// 对结果进行排序：未抢单排在前、已抢单排在后＋相同情况下根据时间排序
		List<NeedEntity> list_0 = new ArrayList<NeedEntity>();//专门存放state为0的记录
		List<NeedEntity> list_1 = new ArrayList<NeedEntity>();//专门存放state为1的记录
		List<NeedEntity> list_2 = new ArrayList<NeedEntity>();//专门存放state为2的记录
		// 所有的记录
		List<NeedEntity> list_all = imp.getList();
		for(NeedEntity e : list_all){
			if(e.getState()==0)
				list_0.add(e);
			else
				list_1.add(e);
		}
				
		// 将存放所有记录的list_all清空，然后把三个list按照顺序存进去
		list_all.clear();
		list_all.addAll(list_0);
		list_all.addAll(list_1);
		
		
		return list_all;
	}
	
	
	/**
	 * 获取指定provider的个人信息
	 * @param provider_id
	 * @return
	 */
	public UserEntity getUserEntityByProviderId(int provider_id){
		// 健壮性判断
		if (provider_id <= 0) {
			this.result = false;
			this.reason = "provider_id不能<=0";
			return null;
		}
		
		// 获取个人信息
		return CoreDao.queryUniqueById(provider_id, Parameter.UserEntity);
	}
	
	
	
	/**
	 * 修改指定provider的擅长的技术
	 * @param provider_id
	 * @param skill
	 * @return
	 */
	public boolean updateSkillByProviderId(int provider_id,String skill){
		// 健壮性判断
		if (provider_id <= 0 || skill==null || skill.equals("")) {
			this.result = false;
			this.reason = "provider_id不能<=0、skill不能为空";
			return false;
		}
		
		// 修改
		PostDaoUpdateSkillByProviderIdImp imp = new PostDaoUpdateSkillByProviderIdImp(skill, provider_id);
		return imp.hibernateOperation();
	}
	
	
	/**
	 * (Android)查询need表未发送的信息，并设为已发送
	 */
	public List<MsgEntity> queryUnPushNeederMsgList(){
		PostDaoQueryUnPushNeederMsgListImp imp = new PostDaoQueryUnPushNeederMsgListImp();
		this.result = imp.hibernateOperation();
		
		// 将短信装入MsgEntity
		List<MsgEntity> msgList = new ArrayList<MsgEntity>();
		for (NeedEntity e : imp.getList()) {
			MsgEntity msgEntity = new MsgEntity();
			msgEntity.setPhone(e.getNeeder_phone());
			msgEntity.setContent("[毕设帮]您的订单已被"+e.getProvider_name()+"抢单，赶紧查看订单详情吧："+ Parameter.IndexPage);
			msgList.add(msgEntity);
		}
		return msgList;
	}

	
	/**
	 * 获取某一条需求的状态(提高赏金前判断)
	 * @param parseInt
	 * @return
	 */
	public int getStateById(int require_id) {
		NeedEntity entity = CoreDao.queryUniqueById(require_id, Parameter.NeedEntity);
		return entity.getState();
	}
	
	
	
	
	
	
	
	public boolean getResult() {
		return result;
	}
	public String getReason() {
		return reason;
	}


	/**
	 * 管理员获取求助者发布的信息
	 */
	public List<NeedEntity> getNeedEntityList(int state) {
		//查询求助者的需求列表
		String hql = "";
		if(state == -1)
			hql = "from NeedEntity";
		else
			hql = "from NeedEntity where state="+state;
		List<NeedEntity> list = CoreDao.queryListByHql(hql);
		if(list==null){
			this.result = false;
			this.reason = "发布信息失败";
			return null;
		}
		
		return list;
	}


	
	/**
	 * 发布/修改一份合同
	 * @param contract
	 * @param require_id
	 */
	public void postContract(String contract, String require_id) {
		//更新need表中require_id这条记录的contract字段
		PostDaoPostContract imp = new PostDaoPostContract(contract, require_id);
		result = imp.hibernateOperation();
		
		//通知求助者去确认合同
		TemplateMsg.sendTemplateMsg_confirmContract(imp.getNeedEntity());
		
		if(!result)
			this.reason = "数据库更新失败";
	}


	
	/**
	 * 求助者确认合同
	 * @param require_id
	 */
	public int confirmContract(String require_id,String skill) {
		//1.获取符合条件的大神
		List<UserEntity> providerList = getAllMatchProvider(skill);
		
		//2.将List<UserEntity>——>List<NeedHelpEntity>
		List<NeedHelpEntity> needHelpList = new ArrayList<NeedHelpEntity>();
		for(UserEntity e : providerList){
			NeedHelpEntity needHelpEntity = new NeedHelpEntity();
			needHelpEntity.setProvider_id(e.getId());
			needHelpEntity.setProvider_name(e.getName());
			needHelpEntity.setProvider_phone(e.getPhone());
			needHelpEntity.setProvider_skill(e.getSkill());
			needHelpEntity.setProvider_weixin(e.getWeixin_id());
			needHelpList.add(needHelpEntity);
		}
		
		//3.将匹配结果存入need_help表中，4.更新need表中的状态0，5.查询need表中该条记录的全部信息
		PostDaoMatchProviderImp imp = new PostDaoMatchProviderImp(require_id,needHelpList);
		this.result = imp.hibernateOperation();
		if(!result)
			this.reason = imp.getReason();
		
		//6.发送模板消息To求助者(您已确认合同，等待大神抢单)
		TemplateMsg.sendTemplateMsg_successContract(imp.getNeedEntity());
		//7.发送模板消息To大神们(xxx想您求助，赶紧抢单)
		
		for(NeedHelpEntity e : needHelpList)
			TemplateMsg.sendTemplateMsg_grabSingle(e);
		
		return providerList.size();
	}
	
	
	/**
	 * 获取符合条件的大神
	 */
	private List<UserEntity> getAllMatchProvider(String skill){
		//从内存中读取所有的大神
		List<UserEntity> list = Parameter.Providers_Parameters;
		
		// 若Parameter中providers为空，则主动去DB中查一下，再放到Parameter中
		if (list == null) {
			System.out.println("Parameter中没有大神信息，正在去数据库查……");
			list = this.getAllProviderList();
			Parameter.Providers_Parameters = list;
		}
		
		
		// 进行匹配
		// 获取needer的skill数组
		List<Integer> needer_skill_int = String2IntList(skill);
		System.out.println("needer_skill_int＝" + needer_skill_int.toString());
		// 用于存放匹配成功的providers
		List<UserEntity> success_providers = new ArrayList<UserEntity>();
		// 遍历所有的provider
		for (UserEntity e : list) {
			// 将每一个provider_skill转换成int数组
			List<Integer> provider_skill_int = String2IntList(e.getSkill());
			for (int i = 0; i < provider_skill_int.size(); i++) {
				int c = provider_skill_int.get(i) - needer_skill_int.get(i);
				if (c < 0) {
					break;
				}
				if ((i + 1) == provider_skill_int.size()) {
					success_providers.add(e);
				}
			}
		}
		
		return success_providers;
	}
	
	
	/**
	 * 将Stirng——>int数组
	 * @param str
	 * @return
	 */
	private List<Integer> String2IntList(String str){
		if(str==null)
			return null;
		
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<str.length();i++){
			char c = str.charAt(i);
			list.add(Integer.parseInt(c+""));
		}
		
		return list;
	}


	/**
	 * 大神放弃此单
	 * @param require_id
	 */
	public void providerGiveupOrderPre(String require_id,String provider_name) {
		//修改need表中该条记录，把大神信息清空
		this.result = CoreDao.updateByHql("update NeedEntity set provider_id=0,provider_weixin=null,provider_name=null,provider_phone=null,provider_skill=null where id="+require_id);
		if(!result){
			this.reason = "清空旧大神信息时失败";
			return;
		}
		
		//获取求助者skill
		NeedEntity needEntity = CoreDao.queryUniqueById(Long.parseLong(require_id), Parameter.NeedEntity);
		
		//重新匹配大神
		//1.获取符合条件的大神
		List<UserEntity> providerList = getAllMatchProvider(needEntity.getNeeder_skill());
		
		//2.将List<UserEntity>——>List<NeedHelpEntity>
		List<NeedHelpEntity> needHelpList = new ArrayList<NeedHelpEntity>();
		for(UserEntity e : providerList){
			NeedHelpEntity needHelpEntity = new NeedHelpEntity();
			needHelpEntity.setProvider_id(e.getId());
			needHelpEntity.setProvider_name(e.getName());
			needHelpEntity.setProvider_phone(e.getPhone());
			needHelpEntity.setProvider_skill(e.getSkill());
			needHelpEntity.setProvider_weixin(e.getWeixin_id());
			needHelpList.add(needHelpEntity);
		}
		
		//3.将匹配结果存入need_help表中，4.更新need表中的状态0，5.查询need表中该条记录的全部信息
		PostDaoMatchProviderImp imp = new PostDaoMatchProviderImp(require_id,needHelpList);
		this.result = imp.hibernateOperation();
		if(!result)
			this.reason = imp.getReason();
		
		//6.发送模板消息To求助者(大神已放弃订单，但系统以为你重新匹配)
		imp.getNeedEntity().setProvider_name(provider_name);
		TemplateMsg.sendTemplateMsg_proGiveupOrderToNe(imp.getNeedEntity());
		//7.发送模板消息To大神们(xxx想您求助，赶紧抢单)
		for(NeedHelpEntity e : needHelpList)
			TemplateMsg.sendTemplateMsg_grabSingle(e);
	}


	
	/**
	 * 大神确认可以开始服务
	 * @param require_id
	 */
	public void providerConfirm(String require_id) {
		//更新状态，并获取NeedEntity
		UpdateStateAndGetEntityImp imp = new UpdateStateAndGetEntityImp(6,Long.parseLong(require_id));
		imp.hibernateOperation();
		
		//向大神发送确认订单的通知
		TemplateMsg.sendTemplateMsg_proConfirmOrderToPro(imp.getNeedEntity());
		//向求助者发送付款通知
		TemplateMsg.sendTemplateMsg_proConfirmOrderToNe(imp.getNeedEntity());
	}


	
	/**
	 * 求助者付款成功
	 * @param require_id
	 */
	public void neederPay(String require_id) {
		//更新状态，并获取NeedEntity
		UpdateStateAndGetEntityImp imp = new UpdateStateAndGetEntityImp(7,Long.parseLong(require_id));
		imp.hibernateOperation();
		
		//向大神发送开发中的通知
		TemplateMsg.sendTemplateMsg_paySuccessToPro(imp.getNeedEntity());
		//向求助者发送付款成功通知
		//由微信支付发送……
	}


	
	
	/**
	 * 大神完成开发
	 * @param require_id
	 */
	public void finishDevelop(String require_id) {
		//更新状态，并获取NeedEntity
		UpdateStateAndGetEntityImp imp = new UpdateStateAndGetEntityImp(8,Long.parseLong(require_id));
		imp.hibernateOperation();
		
		//向大神发送开发完成的通知
		TemplateMsg.sendTemplateMsg_finishDevToPro(imp.getNeedEntity());
		//向求助者发送开发完成通知
		TemplateMsg.sendTemplateMsg_finishDevToNe(imp.getNeedEntity());
	}


	
	
	/**
	 * 求助者点击通过验收
	 * @param require_id
	 * @param password
	 */
	public void confirmOrder(String require_id, String password,String needer_id) {
		PostDaoConfirmOrderImp imp = new PostDaoConfirmOrderImp(require_id,password,needer_id);
		imp.hibernateOperation();
		if(!imp.getResult()){
			this.result = false;
			this.reason = imp.getReason();
			return;
		}
		
		//向大神发送开发完成的通知
		TemplateMsg.sendTemplateMsg_finishOrderToPro(imp.getNeedEntity());
		//向求助者发送开发完成通知
		TemplateMsg.sendTemplateMsg_finishOrderToNe(imp.getNeedEntity());
	}


	
	/**
	 * 求助者/大神申请仲裁
	 * @param require_id
	 * @param content
	 */
	public void applyArbitration(String require_id, String content) {
		
	}
}
