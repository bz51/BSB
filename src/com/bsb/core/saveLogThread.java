package com.bsb.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class saveLogThread implements Runnable {
	private String content;
	private String level;
	
	
	
	public saveLogThread(String content, String level) {
		super();
		this.content = content;
		this.level = level;
	}



	@Override
	public void run() {
		File file = new File(Parameter.LOG_FILE_PATH);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//将内容写入文件
		try {
			BufferedWriter buf_w = new BufferedWriter(new FileWriter(file,true));
			
			buf_w.write(this.getNowTime()+"｜【"+this.level+"】"+"｜"+this.content);
			buf_w.newLine();
			buf_w.flush();
			buf_w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	private String getNowTime(){
		Date d = new Date();  
        SimpleDateFormat ss = new SimpleDateFormat("MM-dd HH:mm:ss");
        return "【"+ss.format(d)+"】";
	}

}
