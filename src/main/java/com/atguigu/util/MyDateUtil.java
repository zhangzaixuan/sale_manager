package com.atguigu.util;

import java.util.Calendar;
import java.util.Date;

public class MyDateUtil {

	public static Date getMydate(int i) {


		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, 3);
		Date time = calendar.getTime();
		return time;
	}

}
