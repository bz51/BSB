package com.bsb.core;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.apache.struts2.interceptor.ApplicationAware;

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
        
        //定时线程2:每隔30分钟更新一次大神列表
        new PostAction().startGetProviders(); 
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
	
	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}
	
	public Map<String, Object> getApplication() {
		return application;
	}
}
