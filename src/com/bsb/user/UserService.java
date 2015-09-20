package com.bsb.user;

import java.util.ArrayList;
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
		UserDaoSaveCode_PhoneImp imp = new UserDaoSaveCode_PhoneImp(entity);
		return imp.hibernateOperation();
	}
	
	
	/**
	 * (Android)查询所有未发送的验证码短信,并设为已发送
	 * @return
	 */
	public List<MsgEntity> queryUnSendAuthCodeMsgList(){
		//进行查询
		UserDaoQueryMsgListImp imp = new UserDaoQueryMsgListImp();
		this.result = imp.hibernateOperation();
		
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
	public UserEntity saveUserEntity(UserEntity userEntity){
		//健壮性判断
		if(userEntity==null || userEntity.getName()==null || userEntity.getName().equals("")
				|| userEntity.getPassword()==null || userEntity.getPassword().equals("")
				|| userEntity.getPhone()==null || userEntity.getPhone().equals("")
				|| userEntity.getRole()==0 || userEntity.getSkill()==null || userEntity.getSkill().equals("")
				|| userEntity.getTime()==null){
			this.result = false;
			this.reason = "name、password、phone、role、skil、time不能为空";
			return null;
		}
		
		//执行操作
		long id = CoreDao.save(userEntity);
		
		//若插入失败
		if(id<=0){
			this.result = false;
			this.reason = "保存用户信息失败";
			return null;
		}
		
		else{
			userEntity.setId((int) id);
			return userEntity;
		}
	}
	
	
	/**
	 * 用户登录鉴权
	 * @param phone
	 * @param password
	 * @return
	 */
	public UserEntity signin(String phone,String password){
		//健壮性判断
		if(phone==null || phone.equals("") || password==null || password.equals("")){
			this.result = false;
			this.reason = "phone、password不能为空";
			return null;
		}
		
		//执行操作
		String hql = "from "+Parameter.UserEntity+" where phone='"+phone+"' and password='"+password+"' and state=1";
		List<UserEntity> list = CoreDao.queryListByHql(hql);
		
		//判断结果
		if(list!=null && list.size()>0)
			return list.get(0);
		
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
