package com.bsb.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.struts2.interceptor.ApplicationAware;

import com.bsb.core.LogTool;
import com.bsb.core.MyLog;
import com.bsb.core.Parameter;
import com.bsb.entity.MsgEntity;
import com.bsb.entity.NeedEntity;
import com.bsb.entity.NeedHelpEntity;
import com.bsb.entity.UserEntity;
import com.opensymphony.xwork2.ActionSupport;

public class PostAction extends ActionSupport implements ApplicationAware{
	private PostService service = new PostService();
	private Map<String,Object> application;
	private NeedEntity needEntity;
	private List<MsgEntity> msgList;
	private String require_id;
	private UserEntity userEntity;
	private List<NeedEntity> needList;
	private String needer_id;
	private String provider_id;
	private int count;
	private int state;
	private String result = "yes";
	private String reason;
	
	/**
	 * 开启“获取所有入驻的provider”线程(线程内部每隔1小时获取一次)
	 */
	public String startGetProviders(){
//		LogTool.getLogger().info("#准备开启# 线程【获取所有入驻的provider】");
		//Mylog.log(Parameter.INFO,"线程【获取所有入驻的provider】#准备开启# ");
		
		Timer timer = new Timer();
        TimerTask task =new TimerTask(){
            public void run(){
            	//查出所有providers
                List<UserEntity> list = service.getAllProviderList();
                //查询失败，再尝试三次，三次失败就等待下一次
                if(list==null){
                	//Mylog.log(Parameter.ERROR,"线程【获取所有入驻的provider】#查询失败或结果为空，准备三次尝试# ");
                	for(int i=0;i<3;i++){
                		list = service.getAllProviderList();
                		if(list!=null){
                			//Mylog.log(Parameter.INFO,"线程【获取所有入驻的provider】#查询成功，第"+i+1+"次尝试成功# ");
                			break;
                		}
                		//Mylog.log(Parameter.ERROR,"线程【获取所有入驻的provider】#查询失败或结果为空，第"+i+1+"次尝试失败或仍为空# ");
                	}
                }
                //将providers存入application
                application.put(Parameter.ProviderList, list);
//                LogTool.getLogger().info("#已经开启# 线程【获取所有入驻的provider】，当前Application中的provider数："+list.size());
                //Mylog.log(Parameter.INFO,"线程【获取所有入驻的provider】#已经开启，当前Application中provider人数："+list.size()+"# ");
            }
        };
        timer.scheduleAtFixedRate(task, new Date(),1800000);//当前时间开始起动 每次间隔2秒再启动
		return "startGetProviders";
	}
	
	
	/**
	 * 发布一个需求
	 */
	public String postNeed(){
		//健壮性判断
		if(needEntity==null || needEntity.getContent()==null || needEntity.getContent().equals("")
				|| needEntity.getMoney()<=0 || needEntity.getNeeder_id()<=0 || needEntity.getNeeder_name()==null
				|| needEntity.getNeeder_name().equals("") || needEntity.getNeeder_phone()==null
				|| needEntity.getNeeder_phone().equals("") || needEntity.getNeeder_skill()==null || needEntity.getNeeder_skill().equals("")
				|| needEntity.getTitle()==null || needEntity.getTitle().equals("")){
			this.result = "no";
			this.reason = "content、title、needer_id、needer_name、needer_phone、needer_skill、needer_title不能为空";
			//Mylog.log(Parameter.ERROR,"【发布一个需求】#参数为空# ");
			return "postNeed";
		}
		
		//获取application中的所有providers
		List<UserEntity> list = (List<UserEntity>) application.get(Parameter.ProviderList);
		//若application中providers为空，则主动去DB中查一下，再放到application中
		if(list==null){
			list = service.getAllProviderList();
			application.put(Parameter.ProviderList, list);
			//Mylog.log(Parameter.ERROR,"【发布一个需求】#当前application中的大神信息为空，已经重新获取，当前大神人数:"+list.size()+"# ");
		}
		
		//进行匹配
		//获取needer的skill数组
		List<Integer> needer_skill_int = String2IntList(needEntity.getNeeder_skill());
		System.out.println("needer_skill_int＝"+needer_skill_int.toString());
		//用于存放匹配成功的providers
		List<UserEntity> success_providers = new ArrayList<UserEntity>();
		//遍历所有的provider
		for(UserEntity e : list){
			//将每一个provider_skill转换成int数组
			List<Integer> provider_skill_int = String2IntList(e.getSkill());
			for(int i=0;i<provider_skill_int.size();i++){
				int c = provider_skill_int.get(i)-needer_skill_int.get(i);
				if(c<0){
					break;
				}
				if((i+1)==provider_skill_int.size()){
					success_providers.add(e);
				}
			}
		}
		
		
		//将List<UserEntity>——>List<NeedHelpEntity>
		List<NeedHelpEntity> needHelpList = new ArrayList<NeedHelpEntity>();
		for(UserEntity e : success_providers){
			NeedHelpEntity needHelpEntity = new NeedHelpEntity();
			needHelpEntity.setProvider_id(e.getId());
			needHelpEntity.setProvider_name(e.getName());
			needHelpEntity.setProvider_phone(e.getPhone());
			needHelpEntity.setProvider_skill(e.getSkill());
			needHelpList.add(needHelpEntity);
		}
		//插入DB
		boolean result = service.postNeed(needEntity, needHelpList);
		if(!result){
			this.result = "no";
			this.reason = service.getReason();
			//Mylog.log(Parameter.ERROR,"【发布一个需求】#匹配失败##"+needEntity.getNeeder_name()+"匹配失败的原因:"+this.reason+"# ");
			return "postNeed";
		}
		
		//Mylog.log(Parameter.INFO,"【发布一个需求】#成功匹配##为"+needEntity.getNeeder_name()+"匹配到大神人数:"+success_providers.size()+"# ");

		this.count = success_providers.size();
		return "postNeed";
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
	 *  (Android)获取抢单短信
	 */
	public String getUnGrabMsgList(){
		this.msgList = service.queryNowUnGrabMsgList();
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
			//Mylog.log(Parameter.ERROR,"【(Android)获取抢单短信】#失败##原因："+this.reason+"# ");
			return "getUnGrabMsgList";
		}
//		MyLog.log(Parameter.INFO,"【(Android)获取抢单短信】#成功##获取抢单短信条数："+msgList.size()+"# ");
		return "getUnGrabMsgList";
	}
	
	
	/**
	 * 根据require_id获取需求详情
	 */
	public String getNeedDetail(){
		if(require_id==null || require_id.equals("")){
			this.result = "no";
			this.reason = "require_id不能为空";
			//Mylog.log(Parameter.ERROR,"【根据require_id获取需求详情】#失败##原因："+this.reason+"# ");
			return "getNeedDetail";
		}
		
		//获取需求详情
		this.needEntity = service.getNeedDetail(Integer.parseInt(require_id));
		
		//Mylog.log(Parameter.INFO,"【根据require_id获取需求详情】#成功##require_id："+this.require_id+"# ");
		return "getNeedDetail";
	}

	
	/**
	 * provider抢单
	 */
	public String grabSingle(){
		if(require_id==null || require_id.equals("") || userEntity==null
				|| userEntity.getId()<=0 || userEntity.getName()==null
				|| userEntity.getName().equals("")
				|| userEntity.getPhone()==null || userEntity.getPhone().equals("")
				|| userEntity.getSkill()==null || userEntity.getSkill().equals("")){
			this.result = "no";
			this.reason = "require_id、id、name、phone、skill不能为空";
			
			//Mylog.log(Parameter.ERROR,"【provider抢单】#失败##provider_name:"+userEntity==null?"null":userEntity.getName()+",失败原因："+this.reason+"# ");
			return "grabSingle";
		}
		
		boolean result = service.grabSingle(Integer.parseInt(require_id), userEntity);
		if(!result){
			this.result = "no";
			this.reason = service.getReason();
			//Mylog.log(Parameter.ERROR,"【provider抢单】#失败##provider_name:"+userEntity.getName()+",失败原因："+this.reason+"# ");
		}
		
		//Mylog.log(Parameter.INFO,"【provider抢单】#成功##provider_name:"+userEntity.getName()+"抢了订单:"+require_id+"# ");
		return "grabSingle";
	}
	
	
	/**
	 * (Android)查询need表中30分钟未抢单的需求
	 */
	public String query30MUnGrabMsgList(){
		this.msgList = service.query30MUnGrabMsgList();
		
		//Mylog.log(Parameter.INFO,"【(Android)查询need表中30分钟未抢单的需求】");
		
		return "query30MUnGrabMsgList";
	}
	
	
	/**
	 * 提高赏金 
	 */
	public String increaseMoney(){
		if(needEntity==null || needEntity.getId()<=0 || needEntity.getMoney()<=0){
			this.result = "no";
			this.reason = "needEntity.id、needEntity.money不能为空";
			
			//Mylog.log(Parameter.ERROR,"【提高赏金】#失败##neeeder_id:"+userEntity==null?"null":userEntity.getId()+",失败原因："+this.reason+"# ");
			
			return "increaseMoney";
		}
		
		//修改need表中对应require_id的money(修改前首先要判断state是否为0，若为1或2都返回false，提示用户"别提高价格了，已经被抢单了")
		//再修改need_help表中对应require_id的money和post
		
		this.count = service.increaseMoney_new(needEntity.getId(),needEntity.getMoney());
		
		if(count<=0){
			this.result = "no";
			this.reason = service.getReason();
			//Mylog.log(Parameter.ERROR,"【提高赏金】#失败##neeeder_id:"+userEntity.getId()+",失败原因："+this.reason+"# ");
			return "increaseMoney";
		}
		
		//Mylog.log(Parameter.INFO,"【提高赏金】#成功##neeeder_id:"+userEntity.getId()+",新赏金:"+needEntity.getMoney()+"# ");
		return "increaseMoney";
		/*
		//获取application中的所有providers
		List<UserEntity> list = (List<UserEntity>) application.get(Parameter.ProviderList);
		//若application中providers为空，则主动去DB中查一下，再放到application中
		if(list==null){
			list = service.getAllProviderList();
			application.put(Parameter.ProviderList, list);
			System.out.println("共有大神："+list.size());
		}
		
		//进行匹配
		//获取needer的skill数组
		List<Integer> needer_skill_int = String2IntList(needEntity.getNeeder_skill());
		System.out.println("needer_skill_int:"+needer_skill_int);
		System.out.println("needer_skill_int＝"+needer_skill_int.toString());
		//用于存放匹配成功的providers
		List<UserEntity> success_providers = new ArrayList<UserEntity>();
		//遍历所有的provider
		for(UserEntity e : list){
			//将每一个provider_skill转换成int数组
			List<Integer> provider_skill_int = String2IntList(e.getSkill());
			for(int i=0;i<provider_skill_int.size();i++){
				int c = provider_skill_int.get(i)-needer_skill_int.get(i);
				if(c<0)
					break;
				if((i+1)==provider_skill_int.size())
					success_providers.add(e);
			}
		}
		
		System.out.println("匹配结果："+success_providers.size());

		//将List<UserEntity>——>List<NeedHelpEntity>
		List<NeedHelpEntity> needHelpList = new ArrayList<NeedHelpEntity>();
		for(UserEntity e : success_providers){
			NeedHelpEntity needHelpEntity = new NeedHelpEntity();
			needHelpEntity.setProvider_id(e.getId());
			needHelpEntity.setProvider_name(e.getName());
			needHelpEntity.setProvider_phone(e.getPhone());
			needHelpEntity.setProvider_skill(e.getSkill());
			needHelpList.add(needHelpEntity);
		}
		//插入DB
		boolean result = service.increaseMoney(needEntity, needHelpList);
		if(!result){
			this.result = "no";
			this.reason = service.getReason();
		}
		
		this.count = success_providers.size();
		
		return "increaseMoney";
		*/
	}
	
	
	/**
	 * 放弃订单 
	 */
	public String neederGiveUp(){
		if(require_id==null || require_id.equals("")){
			this.result = "no";
			this.reason = "require_id不能为空";
			
			//Mylog.log(Parameter.ERROR,"【放弃订单】#失败##require_id:"+require_id+",失败原因："+this.reason+"# ");
			
			return "neederGiveUp";
		}
		
		boolean result = service.neederGiveUp(Integer.parseInt(require_id));
		if(!result){
			this.result = "no";
			this.reason = service.getReason();
			//Mylog.log(Parameter.ERROR,"【放弃订单】#失败##require_id:"+require_id+",失败原因："+this.reason+"# ");
		}
		
		
		//Mylog.log(Parameter.INFO,"【放弃订单】#成功##require_id:"+require_id+"# ");
		return "neederGiveUp";
	}
	
	
	/**
	 * 获取指定needer的所有订单情况 
	 */
	public String getOrderListByNeederId(){
		if(needer_id==null || needer_id.equals("")){
			this.result = "no";
			this.reason = "needer_id不能为空";
			
			//Mylog.log(Parameter.ERROR,"【获取指定needer的所有订单情况】#失败##needer_id:"+needer_id+",失败原因："+this.reason+"# ");
			
			return "getOrderListByNeederId";
		}
		
		this.needList = service.getOrderListByNeederId(Integer.parseInt(needer_id));
		//Mylog.log(Parameter.ERROR,"【获取指定needer的所有订单情况】#成功##needer_id:"+needer_id+"# ");
		
		return "getOrderListByNeederId";
	}
	
	
	/**
	 * 获取指定provider的所有订单情况 
	 */
	public String getOrderListByProviderId(){
		if(provider_id==null || provider_id.equals("")){
			this.result = "no";
			this.reason = "provider_id不能为空";
			
			//Mylog.log(Parameter.ERROR,"【获取指定provider的所有订单情况 】#失败##provider_id:"+provider_id+",失败原因："+this.reason+"# ");
			
			return "getOrderListByProviderId";
		}
		
		this.needList = service.getOrderListByProviderId(Integer.parseInt(provider_id));
		
		//Mylog.log(Parameter.INFO,"【获取指定provider的所有订单情况 】#成功##provider_id:"+provider_id+"# ");
		return "getOrderListByProviderId";
	}
	
	
	/**
	 * 获取指定provider的个人信息
	 */
	public String getUserEntityByProviderId(){
		if(provider_id==null || provider_id.equals("")){
			this.result = "no";
			this.reason = "provider_id不能为空";
			
			//Mylog.log(Parameter.ERROR,"【获取指定provider的个人信息】#失败##provider_id:"+provider_id+",失败原因："+this.reason+"# ");
						
			return "getUserEntityByProviderId";
		}
		
		this.userEntity = service.getUserEntityByProviderId(Integer.parseInt(provider_id));
		//Mylog.log(Parameter.INFO,"【获取指定provider的个人信息】#成功##provider_id:"+provider_id+"# ");
		
		return "getUserEntityByProviderId";
	}
	
	
	/**
	 * 修改指定provider的擅长的技术 
	 */
	public String updateSkillByProviderId(){
		if(userEntity==null || userEntity.getId()<=0 || userEntity.getSkill()==null || userEntity.getSkill().equals("")){
			this.result = "no";
			this.reason = "provider_id、skill不能为空";
			//Mylog.log(Parameter.ERROR,"【修改指定provider的擅长的技术】#失败##失败原因："+this.reason+"# ");
			
			return "updateSkillByProviderId";
		}
		
		boolean result = service.updateSkillByProviderId(userEntity.getId(), userEntity.getSkill());
		if(!result){
			this.result = "no";
			this.reason = service.getReason();
			//Mylog.log(Parameter.ERROR,"【修改指定provider的擅长的技术】#失败##失败原因："+this.reason+"# ");
		}
		
		//Mylog.log(Parameter.INFO,"【修改指定provider的擅长的技术】#成功##"+userEntity.getName()+"将技能修改为"+userEntity.getSkill()+"#");
		return "updateSkillByProviderId";
	}
	
	
	/**
	 * (Android)查询need表未发送的信息，并设为已发送 
	 */
	public String queryUnPushNeederMsgList(){
		this.msgList = service.queryUnPushNeederMsgList();
		//Mylog.log(Parameter.INFO,"【(Android)查询need表未发送的信息，并设为已发送】#");
		return "queryUnPushNeederMsgList";
	}
	
	
	/**
	 * 获取某一条需求的状态(提高赏金前判断)
	 */
	public String getStateById(){
		if(require_id==null || "".equals(require_id)){
			this.result = "no";
			this.reason = "require_id不能为空";
			//Mylog.log(Parameter.ERROR,"【获取某一条需求的状态】#失败##失败原因："+this.reason+"# ");
			return "getStateById";
		}
		
		this.state = service.getStateById(Integer.parseInt(require_id));
		if(state<=0){
			this.result = "no";
			//Mylog.log(Parameter.ERROR,"【获取某一条需求的状态】#失败##失败原因："+this.reason+"# ");
			return "getStateById";
		}
		
		//Mylog.log(Parameter.INFO,"【获取某一条需求的状态】#成功##require_id:"+require_id+"# ");
		return "getStateById";
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}
	
	public Map<String, Object> getApplication() {
		return application;
	}


	public NeedEntity getNeedEntity() {
		return needEntity;
	}


	public void setNeedEntity(NeedEntity needEntity) {
		this.needEntity = needEntity;
	}


	public List<MsgEntity> getMsgList() {
		return msgList;
	}


	public void setMsgList(List<MsgEntity> msgList) {
		this.msgList = msgList;
	}


	public String getRequire_id() {
		return require_id;
	}


	public void setRequire_id(String require_id) {
		this.require_id = require_id;
	}


	public UserEntity getUserEntity() {
		return userEntity;
	}


	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}


	public List<NeedEntity> getNeedList() {
		return needList;
	}


	public void setNeedList(List<NeedEntity> needList) {
		this.needList = needList;
	}


	public String getNeeder_id() {
		return needer_id;
	}


	public void setNeeder_id(String needer_id) {
		this.needer_id = needer_id;
	}


	public String getProvider_id() {
		return provider_id;
	}


	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}



	public void setCount(int count) {
		this.count = count;
	}
	
	public static void main(String[] args){
		
	}


	public int getCount() {
		return count;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}
	
	
	
}
