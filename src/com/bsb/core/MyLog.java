package com.bsb.core;

public class MyLog {
	private String log_path = Parameter.LOG_FILE_PATH;/* 日志文件的存放路径 **/
	
	/**
	 * 插入日志
	 * @param content
	 */
	public static void log(String content,String level){
		if(content==null || "".equals(content))
			return;
		
		new Thread(new saveLogThread(level,content)).start();;
	}
	
}
