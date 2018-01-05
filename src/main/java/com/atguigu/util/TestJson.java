package com.atguigu.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;

public class TestJson {

	public static void main(String[] args) {

		// obj to json
		// json to obj

		// list to json
		// json to list

		// T_MALL_SHOPPINGCAR cart1 = new T_MALL_SHOPPINGCAR();
		//
		// cart1.setSku_mch("中文" + 0);
		// cart1.setSku_jg(0);
		//
		// String obj_to_json = obj_to_json(cart1);
		//
		// System.out.println(obj_to_json);
		//
		// T_MALL_SHOPPINGCAR json_to_obje = json_to_obje(obj_to_json,
		// T_MALL_SHOPPINGCAR.class);
		//
		// System.out.println(json_to_obje);

		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

		for (int i = 0; i < 5; i++) {
			T_MALL_SHOPPINGCAR cart = new T_MALL_SHOPPINGCAR();

			cart.setSku_mch("中文" + i);
			cart.setSku_jg(i);

			list_cart.add(cart);

		}

		String list_to_json = list_to_json(list_cart);

		System.out.println(list_to_json);

		json_to_list(list_to_json, T_MALL_SHOPPINGCAR.class);

	}

	public static <T> List<T> json_to_list(String list_json, Class<T> t) {
		String decode = "";
		try {
			decode = URLDecoder.decode(list_json, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Gson gson = new Gson();
		//
		// TypeToken<List<T>> list_t = new TypeToken<List<T>>() {
		// };
		//
		// List<T> fromJson = gson.fromJson(decode, list_t.getType());

		JSONArray jsonArray = JSONArray.fromObject(decode);
		List<T> list = (List<T>) JSONArray.toCollection(jsonArray, t);

		return list;
	}

	public static <T> T json_to_obje(String json, Class<T> t) {
		T obj = null;
		try {
			String decode = URLDecoder.decode(json, "utf-8");

			Gson gson = new Gson();

			obj = gson.fromJson(decode, t);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;
	}

	public static <T> String list_to_json(List<T> t) {

		Gson gson = new Gson();

		String json = "";
		try {
			json = URLEncoder.encode(gson.toJson(t), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	public static <T> String obj_to_json(T t) {

		Gson gson = new Gson();

		String json = "";
		try {
			json = URLEncoder.encode(gson.toJson(t), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

}
