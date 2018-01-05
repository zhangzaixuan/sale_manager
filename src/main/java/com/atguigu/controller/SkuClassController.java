package com.atguigu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.bean.MODEL_T_MALL_SKU_ATTR_VALUE;
import com.atguigu.bean.OBJECT_T_MALL_ATTR;
import com.atguigu.bean.OBJECT_T_MALL_SKU_DETAIL;
import com.atguigu.bean.OBJECT_T_MALL_SKU_KEYWORDS;
import com.atguigu.bean.T_MALL_SKU;
import com.atguigu.bean.T_MALL_SKU_ATTR_VALUE;
import com.atguigu.bean.T_MALL_SKU_CLASS;
import com.atguigu.service.AttrServiceInfo;
import com.atguigu.service.SkuServiceInfo;
import com.atguigu.util.MyCacheUtil;
import com.atguigu.util.MyHttpGetUtil;
import com.atguigu.util.MyJsonUtil;
import com.atguigu.util.MyPorpertiesUtil;

@Controller
public class SkuClassController {
	@Autowired
	SkuServiceInfo skuServiceInfo;
	@Autowired
	AttrServiceInfo attrServiceInfo;

	@RequestMapping("solr_search")
	public String search_solr(String keywords, ModelMap map) {

		List<OBJECT_T_MALL_SKU_KEYWORDS> list_search = new ArrayList<OBJECT_T_MALL_SKU_KEYWORDS>();

		try {
			String doGet = MyHttpGetUtil
					.doGet(MyPorpertiesUtil.getMypath("keywords.properties", "sku_keywords") + keywords + ".do");
			list_search = MyJsonUtil.json_to_list(doGet, OBJECT_T_MALL_SKU_KEYWORDS.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("list_sku", list_search);
		map.put("keywords", keywords);

		return "sale_search_keywords_list";
	}

	@RequestMapping("add_cache")
	@ResponseBody
	public String add_cache() {

		List<Integer> list_attr_id = attrServiceInfo.get_attr_id();

		for (int i = 0; i < list_attr_id.size(); i++) {
			Integer attr_id = list_attr_id.get(i);
			List<Integer> list_value_id = attrServiceInfo.get_value_id(attr_id);
			for (int j = 0; j < list_value_id.size(); j++) {

				String key = "attr_" + 23 + "_" + attr_id + list_value_id.get(j);
				T_MALL_SKU_ATTR_VALUE attr_VALUE = new T_MALL_SKU_ATTR_VALUE();
				attr_VALUE.setShxm_id(attr_id);
				attr_VALUE.setShxzh_id(list_value_id.get(j));
				List<T_MALL_SKU_ATTR_VALUE> list_av = new ArrayList<T_MALL_SKU_ATTR_VALUE>();
				list_av.add(attr_VALUE);
				MyCacheUtil.setListRedis(key, list_av);
			}
		}

		return "redis_cache";
	}

	/*
	 * @RequestMapping("attr_search") public String attr_searchtea(int
	 * class_2_id,@RequestParam("string_array") String[] string_array, ModelMap map)
	 * { List<T_MALL_SKU_ATTR_VALUE> list_sku=new
	 * ArrayList<T_MALL_SKU_ATTR_VALUE>(); for(int i=0;i<string_array.length;i++) {
	 * 
	 * String array=string_array[i]; String[] new_array = array.split("_");
	 * T_MALL_SKU_ATTR_VALUE av = new T_MALL_SKU_ATTR_VALUE();
	 * av.setShxm_id(Integer.parseInt(new_array[0]));
	 * av.setShxzh_id(Integer.parseInt(new_array[1])); list_sku.add(av); }
	 * map.put("list_sku", list_sku); return "sale_search_sku_list"; }
	 */
	@RequestMapping("attr_search")
	public String attr_search(int class_2_id, MODEL_T_MALL_SKU_ATTR_VALUE list_av, ModelMap map) {
		List<T_MALL_SKU_CLASS> list_sku = new ArrayList<T_MALL_SKU_CLASS>();
		List<T_MALL_SKU_ATTR_VALUE> list_sku_av = list_av.getList_av();

		list_sku = MyCacheUtil.getListRedis(
				"attr_" + class_2_id + "_" + list_sku_av.get(0).getShxm_id() + "_" + list_sku_av.get(0).getShxzh_id(),
				T_MALL_SKU_CLASS.class);

		// 前台过来的数据最好先封装好！

		// list_sku= skuServiceInfo.get_attr_search(class_2_id,list_av.getList_av());
		map.put("list_sku", list_sku);
		return "sale_search_sku_list";
	}

	@RequestMapping("sku_detail")
	public String sku_detail(int spu_id, int sku_id, ModelMap map) {

		OBJECT_T_MALL_SKU_DETAIL obj_sku = skuServiceInfo.get_sku_search(spu_id, sku_id);

		List<T_MALL_SKU> list_sku = skuServiceInfo.get_sku_list_by_spu_id(spu_id);
		map.put("list_sku", list_sku);
		map.put("obj_sku", obj_sku);
		return "sale_search_detail";
	}

	@RequestMapping("goto_class_search")
	public String get_sku_attr_list(int class_2_id, String class_2_name, ModelMap map) {
		List<T_MALL_SKU_CLASS> list_sku = new ArrayList<T_MALL_SKU_CLASS>();
		List<OBJECT_T_MALL_ATTR> list_attr = attrServiceInfo.get_list_attr_byclass_2_id(class_2_id);

		String key = "class_2_" + class_2_id;
		list_sku = MyCacheUtil.getListRedis(key, T_MALL_SKU_CLASS.class);

		if (list_sku == null || list_sku.size() == 0) {
			list_sku = skuServiceInfo.get_sku_by_class_2(class_2_id);
			MyCacheUtil.setListRedis(key, list_sku);
		}
		map.put("list_sku", list_sku);
		map.put("list_attr", list_attr);
		map.put("class_2_id", class_2_id);
		map.put("class_2_name", class_2_name);
		return "sale_search";
	}

}
