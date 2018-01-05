package com.atguigu.util;

import java.math.BigDecimal;
import java.util.List;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;

public class MyCartSumUtil {

	public static BigDecimal caleCartSum(List<T_MALL_SHOPPINGCAR> list_cart) {
		
//		后续把泛型加上；

		BigDecimal sum = new BigDecimal("0");

		for (int i = 0; i < list_cart.size(); i++) {

			if (("1").equals(list_cart.get(i).getShfxz())) {
				
				sum = sum.add(new BigDecimal(list_cart.get(i).getHj() + ""));
			}
		}
		return sum;

	}

}
