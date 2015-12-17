package com.bsb.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bsb.core.CoreDao;
import com.bsb.core.Parameter;
import com.bsb.entity.AuthCodeEntity;
import com.bsb.entity.MsgEntity;
import com.bsb.entity.UserEntity;

public class UserService {
	private boolean result = true;
	private String reason;
	
	/**
	 * 保存验证信息(将phone、Action生成的code存入db)
	 * @param phone
	 * @param code
	 * @return
	 */
	public boolean saveCode_Phone(String phone,String code){
		//健壮性判断
		if(phone==null || "".equals(phone) || code==null || "".equals(code)){
			this.result = false;
			this.reason = "phone、code不能为空";
			return false;
		}
		
		//将phone、code装入AuthCodeEntity
		AuthCodeEntity entity = new AuthCodeEntity();
		entity.setCode(code);
		entity.setPhone(phone);
		
		//将AuthCodeEntity存入DB
		UserDaoSendCodeByAlidayu_PhoneImp imp = new UserDaoSendCodeByAlidayu_PhoneImp(entity);
		boolean db_result = imp.hibernateOperation();
		
		//对执行结果进行判断
		if(!db_result || !imp.getResult()){
			this.result = false;
			this.reason = imp.getReason();
			return false;
		}else
			return true;
	}
	
	
	/**
	 * (Android)查询所有未发送的验证码短信,并设为已发送
	 * @return
	 */
	public List<MsgEntity> queryUnSendAuthCodeMsgList(){
		//进行查询
		UserDaoQueryMsgListImp imp = new UserDaoQueryMsgListImp();
		this.result = imp.hibernateOperation();
		
		if(!result)
			this.reason = imp.getReason();
		
		//获取结果
		List<AuthCodeEntity> authCodeList = imp.getList();
		List<MsgEntity> msgList = new ArrayList<MsgEntity>();
		for(AuthCodeEntity e : authCodeList){
			MsgEntity entity = new MsgEntity();
			entity.setPhone(e.getPhone());
			entity.setContent(Parameter.AuthCodeContent1+e.getCode()+Parameter.AuthCodeContent2);
			msgList.add(entity);
		}
		
		return msgList;
	}
	
	
	/**
	 * 保存用户信息
	 * @param userEntity
	 * @return
	 */
	public int saveUserEntity(UserEntity userEntity){
		//健壮性判断
		if(userEntity==null || userEntity.getName()==null || userEntity.getName().equals("")
				|| userEntity.getPassword()==null || userEntity.getPassword().equals("")
				|| userEntity.getPhone()==null || userEntity.getPhone().equals("")){
			this.result = false;
			this.reason = "name、password、phone、role、skil、time不能为空";
			return 0;
		}
		
		//执行操作
		userEntity.setState(1);
		userEntity.setTime(new Timestamp(new Date().getTime()));
		int id = CoreDao.save(userEntity);
		
		//若插入失败
		if(id<=0){
			this.result = false;
			this.reason = "保存用户信息失败";
			return 0;
		}
		
		else{
			return id;
		}
	}
	
	
	/**
	 * 用户登录鉴权
	 * @param phone
	 * @param password
	 * @return
	 */
	public UserEntity signin(String phone,String password,String open_id){
		//健壮性判断
		if(phone==null || phone.equals("") || password==null || password.equals("")){
			this.result = false;
			this.reason = "phone、password不能为空";
			return null;
		}
		
		//登录鉴权
		String hql = "from "+Parameter.UserEntity+" where phone='"+phone+"' and password='"+password+"' and state=1";
		List<UserEntity> list = CoreDao.queryListByHql(hql);
		
		//判断结果，若该用户存在
		if(list!=null && list.size()>0){
			UserEntity entity = list.get(0);
			//判断该用户的open_id和获取到的open_id是否相同，若不同再更新
			if(!open_id.equals(entity.getWeixin_id())){
				//将open_id存入user表
				CoreDao.updateByHql("update UserEntity set weixin_id='"+open_id+"' where id="+entity.getId());
			}
			return entity;
		}
		
		else{
			this.result = false;
			this.reason = Parameter.SigninFailure;//用户名或密码错误
			return null;
		}
	}

	public boolean getResult() {
		return result;
	}

	public String getReason() {
		return reason;
	}
	
	
}
