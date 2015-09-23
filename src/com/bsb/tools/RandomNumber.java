package com.bsb.tools;

import java.util.Random;

public class RandomNumber {
	
	/**
	 * 返回长度为【strLength】的随机数，在前面补0 
	 * @param strLength
	 * @return
	 */
	public static String getFixLenthString(int strLength) {  
	      
	    Random rm = new Random();  
	      
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(1, strLength + 1);  
	} 
}
