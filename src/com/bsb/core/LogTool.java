package com.bsb.core;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogTool {
	
	public static Logger getLogger(){
//		createFile("/usr/tomcat/webapps/logs/bsb_debug.log");
//		createFile("/usr/tomcat/webapps/logs/bsb_error.log");
		File f = new File(LogTool.class.getResource("").getPath()); 
		File f2 = new File(LogTool.class.getResource("/").getPath()); 
		System.out.println(f); 
		System.out.println(f2); 
		PropertyConfigurator.configure("/log4j.properties");
        return Logger.getLogger(LogTool.class);
        
	}
	
	/**
	 * 创建文件
	 * @param filepath 文件路径
	 * @return File对象
	 * @author chaibozhou
	 */
	private static File createFile(String filepath)
	{
		File f = new File(filepath);
		if(!f.exists())
		{
			try {
				if(!f.createNewFile())
				{
					return null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}
}
