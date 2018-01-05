package com.atguigu.util;

import java.io.IOException;
import java.util.Properties;

public class MyPorpertiesUtil {

	public static String getMypath(String source_pro, String key) {
		
		
		Properties properties = new Properties();
		try {
			properties.load(MyPorpertiesUtil.class.getClassLoader().getResourceAsStream(source_pro));
//			
		} catch (IOException e) {
			e.printStackTrace();
		}
		String property = properties.getProperty(key);
		return property;	
	}

}
