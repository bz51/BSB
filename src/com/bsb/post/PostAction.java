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
import com.bsb.wechat.TemplateMsg;
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
	private String contract;
	private String skill;
	private String provider_name;
	private String needer_name;
	private String password;
	private String content;
	private String role;
	private String name;
	private String user_id;
	private String phone;
	private String pic_id;
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
//                application.put(Parameter.ProviderList, list);
                //将providers存入Parameter
                Parameter.Providers_Parameters = list;
//                LogTool.getLogger().info("#已经开启# 线程【获取所有入驻的provider】，当前Application中的provider数："+list.size());
                //Mylog.log(Parameter.INFO,"线程【获取所有入驻的provider】#已经开启，当前Application中provider人数："+list.size()+"# ");
                
                System.out.println("Parameter中所有大神数="+Parameter.Providers_Parameters.size());
            }
        };
        timer.scheduleAtFixedRate(task, new Date(),1800000);//当前时间开始起动 每次间隔30分钟再启动
		return "startGetProviders";
	}
	
	
	/**
	 * 发布一个需求
	 */
	/**
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
		
		//获取Parameter中的所有providers
		List<UserEntity> list = Parameter.Providers_Parameters;
		//若Parameter中providers为空，则主动去DB中查一下，再放到Parameter中
		if(list==null){
			System.out.println("Parameter中没有大神信息，正在去数据库查……");
			list = service.getAllProviderList();
			Parameter.Providers_Parameters = list;
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
			needHelpEntity.setProvider_weixin(e.getWeixin_id());
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
	*/
	
	
	/**
	 * 发布一条信息（现用）
	 */
	public String postNeed(){
		// 健壮性判断
		if (needEntity == null || needEntity.getContent() == null || needEntity.getContent().equals("")
				|| needEntity.getMoney() <= 0 || needEntity.getNeeder_id() <= 0 || needEntity.getNeeder_name() == null
				|| needEntity.getNeeder_name().equals("") || needEntity.getNeeder_phone() == null
				|| needEntity.getNeeder_phone().equals("") || needEntity.getNeeder_skill() == null
				|| needEntity.getNeeder_skill().equals("") || needEntity.getTitle() == null
				|| needEntity.getTitle().equals("")) {
			this.result = "no";
			this.reason = "content、title、needer_id、needer_name、needer_phone、needer_skill、needer_title不能为空";
			return "postNeed";
		}		
		
		//发布一条信息
		service.postNeed(needEntity,this.pic_id);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
			
		return "postNeed";
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
		System.out.println("result_action="+result);
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
	
	
	
	
	/**
	 * 管理员获取求助者发布的信息
	 */
	public String getNeedEntityList(){
		//获取求助者发布的信息
		this.needList = service.getNeedEntityList(state);
		this.result = service.getResult()+"";
		this.reason = service.getReason();
		return "getNeedEntityList";
	}
	
	
	
	/**
	 * 管理员发布/修改一份合同
	 */
	public String postContract(){
		//健壮性判断
		if(this.contract==null || "".equals(contract) || this.require_id==null || "".equals(require_id)){
			this.result = "no";
			this.reason = "contract、require_id不能为空";
			return "postContract";
		}
		
		//发布/修改一份合同
		service.postContract(contract,require_id);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		
		return "postContract";
	}
	
	
	
	/**
	 * 求助者确认合同
	 */
	public String confirmContract(){
		//健壮性判断
		if(require_id==null || "".equals(require_id) || skill==null || "".equals(skill)){
			this.result = "no";
			this.reason = "require_id、skill不能为空";
			return "confirmContract";
		}
		
		//求助者确认合同
		this.count = service.confirmContract(this.require_id,this.skill);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		return "confirmContract";
	}
	
	
	
	/**
	 * 大神放弃此单(确认订单前放弃)
	 */
	public String providerGiveupOrderPre(){
		//健壮性判断
		if(this.require_id==null || "".equals(this.require_id) || this.provider_name==null || "".equals(this.provider_name)){
			this.result = "no";
			this.reason = "require_id、provider_name不能为空";
			return "providerGiveupOrderPre";
		}
		
		//大神放弃此单
		service.providerGiveupOrderPre(this.require_id,this.provider_name);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		return "providerGiveupOrderPre";
	}
	
	
	
	/**
	 * 大神确认可以开始服务 
	 */
	public String providerConfirm(){
		//健壮性判断
		if(this.require_id==null || "".equals(this.require_id)){
			this.result = "no";
			this.reason = "require_id不能为空";
			return "providerConfirm";
		}
		
		//大神确认可以开始服务 
		service.providerConfirm(this.require_id);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		return "providerConfirm";
	}
	
	
	
	/**
	 * 求助者重找大神
	 */
	public String chongZhaoProvider(){
		System.out.println("chongZhaoProvider………………………………");
		System.out.println("needer_name="+this.needer_name);
		System.out.println("require_id="+this.require_id);
		//健壮性判断
		if(this.require_id==null || "".equals(this.require_id) || this.needer_name==null || "".equals(this.needer_name)){
			this.result = "no";
			this.reason = "require_id、needer_name不能为空";
			System.out.println(this.reason);
			return "chongZhaoProvider";
		}
		
		//求助者重找大神
		this.needEntity = service.chongZhaoProvider(this.require_id,this.needer_name);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		
		System.out.println("返回的result＝"+this.result);
		return "chongZhaoProvider";
	}
	
	
	
	/**
	 * 求助者付款成功 
	 */
	public String neederPay(){
		//健壮性判断
		if(this.require_id==null || "".equals(this.require_id)){
			this.result = "no";
			this.reason = "require_id不能为空";
			return "neederPay";
		}
		
		service.neederPay(this.require_id);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		return "neederPay";
	}
	
	
	
	/**
	 * 大神完成开发
	 */
	public String finishDevelop(){
		//健壮性判断
		if(this.require_id==null || "".equals(this.require_id)){
			this.result = "no";
			this.reason = "require_id不能为空";
			return "finishDevelop";
		}
		
		service.finishDevelop(this.require_id);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		return "finishDevelop";
	}
	
	
	
	/**
	 * 求助者点击通过验收
	 */
	public String confirmOrder(){
		//健壮性判断
		if(this.require_id==null || "".equals(this.require_id) || this.password==null || "".equals(this.password) || this.needer_id==null || "".equals(this.needer_id)){
			this.result = "no";
			this.reason = "require_id、password、needer_id不能为空";
			return "confirmOrder";
		}
		
		service.confirmOrder(this.require_id,this.password,this.needer_id);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		
		return "confirmOrder";
	}
	
	
	/**
	 * 求助者/大神申请仲裁
	 */
	public String applyArbitration(){
		//健壮性判断
		if(this.require_id==null || "".equals(this.require_id) || this.content==null || "".equals(this.content) || this.role==null || "".equals(this.role)){
			this.result = "no";
			this.reason = "require_id、content、role不能为空";
			return "applyArbitration";
		}
		
		service.applyArbitration(this.require_id,this.content,this.role);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		
		return "applyArbitration";
	}
	
	
	
	
	/**
	 * 发布一条咨询客服信息
	 * @return
	 */
	public String postFeedBack(){
		//健壮性判断
		if(this.name==null || "".equals(name) || this.user_id==null || "".equals(user_id) || this.phone==null || "".equals(phone) || this.role==null || "".equals(role) || this.content==null || "".equals(content) || this.require_id==null || "".equals(require_id)){
			this.result = "no";
			this.reason = "name、user_id、phone、role、content、require_id均不能为空！";
			return "postFeedBack";
		}
		
		service.postFeedBack(this.name,this.user_id,this.phone,this.role,this.content,this.require_id);
		if(!service.getResult()){
			this.result = "no";
			this.reason = service.getReason();
		}
		
		return "postFeedBack";
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


	public String getContract() {
		return contract;
	}


	public void setContract(String contract) {
		this.contract = contract;
	}


	public String getSkill() {
		return skill;
	}


	public void setSkill(String skill) {
		this.skill = skill;
	}


	public String getProvider_name() {
		return provider_name;
	}


	public void setProvider_name(String provider_name) {
		this.provider_name = provider_name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public PostService getService() {
		return service;
	}


	public void setService(PostService service) {
		this.service = service;
	}


	public String getNeeder_name() {
		return needer_name;
	}


	public void setNeeder_name(String needer_name) {
		this.needer_name = needer_name;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getPic_id() {
		return pic_id;
	}


	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}
	
	
	
}
