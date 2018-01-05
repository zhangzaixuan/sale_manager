package com.atguigu.service;

import java.util.List;

import com.atguigu.bean.OBJECT_T_MALL_ATTR;
import com.atguigu.bean.OBJECT_T_MALL_SKU_CLASS;
import com.atguigu.bean.T_MALL_SKU_ATTR_VALUE;

public interface AttrServiceInfo {

	List<OBJECT_T_MALL_ATTR> get_list_attr_byclass_2_id(int class_2_id);

	void save_attr(List<OBJECT_T_MALL_ATTR> list_attr, int class_2_id);

	List<Integer> get_attr_id();

	List<Integer> get_value_id(Integer attr_id);

	

}
