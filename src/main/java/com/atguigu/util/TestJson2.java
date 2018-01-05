package com.atguigu.util;

import java.util.ArrayList;
import java.util.List;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;

import net.sf.json.JSONArray;

public class TestJson2 {

	public static void main(String[] args) {
		// 1

		// 2

		// 3

		// 4
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

		for (int i = 0; i < 5; i++) {
			T_MALL_SHOPPINGCAR cart = new T_MALL_SHOPPINGCAR();

			cart.setSku_mch("中文" + i);
			cart.setSku_jg(i);

			list_cart.add(cart);

		}

		// 集合转json
		JSONArray jsonArray = JSONArray.fromObject(list_cart);
		String string = jsonArray.toString();
		System.out.println(string);

		// json转集合
		JSONArray jsonArray2 = JSONArray.fromObject(string);
		List<T_MALL_SHOPPINGCAR> list_cart2 = (List<T_MALL_SHOPPINGCAR>) JSONArray.toCollection(jsonArray2,
				T_MALL_SHOPPINGCAR.class);

		System.out.println(list_cart2);
	}

}
