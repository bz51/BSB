package com.bsb.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.apache.struts2.interceptor.ApplicationAware;

import com.bsb.entity.OpenTokenId;
import com.bsb.post.PostAction;
import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

/**
 * 本类用于Tomcat启动时各种初始化操作
 * @author chibozhou
 */
public class InitFilterDispatcher extends StrutsPrepareAndExecuteFilter implements ApplicationAware{
	private Map<String,Object> application;
	
	@Override
	/**
	 * Tomcat启动时各种初始化操作在此进行
	 */
    public void init(FilterConfig arg0) throws ServletException {    
        super.init(arg0);    
        
        System.out.println("InitFilterDispatcher已执行……");
        
        //定时线程1:每隔1.5时获取一次access_token5400000
        getAccess_token(5400000);
        
        //定时线程1:每隔1.5时获取一次ticket
        getTicket(5400000);
        
        //定时线程2:每隔30分钟更新一次大神列表
        new PostAction().startGetProviders();
        
        //读取DB中的Open_token和open_id
        readOpenTokenIdFromDB();
    }    
	
	
	
	/**
	 * 定时线程：获取access_token
	 */
	private boolean getAccess_token(long time){
		if(time<=0)
			return false;
		
		Timer timer = new Timer();
        TimerTask task =new TimerTask(){
            public void run(){
            	//获取access_token
        		String param = "grant_type=client_credential&appid=wx1a4c2e86c17d1fc4&secret=ba95beea6a08dc2abf245ae8d8c7dc3e";
        		String access_token_result = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", param);
        		String access_token = "";
				try {
					access_token = new JSONObject(access_token_result).getString("access_token");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Parameter.AccessToken_Parameters = access_token;
        		System.out.println("获取到的access_token="+access_token);
        		System.out.println("Parameter中的access_token="+Parameter.AccessToken_Parameters);
            }
        };
        timer.scheduleAtFixedRate(task, new Date(),time);//当前时间开始起动 每次间隔n秒再启动
		return true;
	}
	
	
	/**
	 * 定时线程：获取ticket
	 */
	private boolean getTicket(long time){
		if(time<=0)
			return false;
		
		Timer timer = new Timer();
		TimerTask task =new TimerTask(){
			public void run(){
				//获取ticket
				String param = "access_token="+Parameter.AccessToken_Parameters+"&type=jsapi";
				String ticket_result = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", param);
				String ticket = "";
				try {
					ticket = new JSONObject(ticket_result).getString("ticket");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Parameter.Ticket_Parameter = ticket;
				System.out.println("获取到的ticket="+ticket);
				System.out.println("Parameter中的ticket="+Parameter.Ticket_Parameter);
			}
		};
		timer.scheduleAtFixedRate(task, 10000,time);//当前时间开始起动 每次间隔n秒再启动
		return true;
	}
	
	
	
	@Override
	public void destroy() {
		super.destroy();
		System.out.println("detroy……");
		
		//将内存中的open_token和open_id存入DB
		saveOpenTokenIdToDB();
	}


	/**
	 * 将内存中的open_token和open_id存入DB
	 * @return
	 */
	private boolean saveOpenTokenIdToDB(){
		//存储OpenTokenId的List
		List<OpenTokenId> list = new ArrayList<OpenTokenId>();
		//将内存中的open_token和open_id存至数据库
		Set<String> open_tokens  = Parameter.OpenTokenId_Parameters.keySet();
		for(String open_token : open_tokens){
			//获取open_id
			String open_id = Parameter.OpenTokenId_Parameters.get(open_token);
			//将open_id和open_token存入entity
			OpenTokenId entity = new OpenTokenId();
			entity.setOpen_id(open_id);
			entity.setOpen_token(open_token);
			//将当前entity存入List
			list.add(entity);
		}
		//将List存入DB
		return CoreDao.saveList(list);
	}
	
	
	/**
	 * 读取DB中的Open_token和open_id
	 */
	private boolean readOpenTokenIdFromDB(){
		//从DB中读取所有OpenTokenId
		List<OpenTokenId> list = new ArrayList<OpenTokenId>();
		list = CoreDao.queryListByHql("from OpenTokenId");
		
		//将所有的OpenTokenId存入内存
		Map<String,String> map = new HashMap<String,String>();
		for(OpenTokenId e : list){
			map.put(e.getOpen_token(), e.getOpen_id());
		}
		
		Parameter.OpenTokenId_Parameters = map;
		
		//打印
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			System.out.println(key+"="+map.get(key));
		}
		
		return true;
	}
	

	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}
	
	public Map<String, Object> getApplication() {
		return application;
	}
}
