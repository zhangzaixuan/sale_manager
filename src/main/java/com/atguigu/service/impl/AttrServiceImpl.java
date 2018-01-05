package com.atguigu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.bean.OBJECT_T_MALL_ATTR;
import com.atguigu.mapper.AttrMapper;
import com.atguigu.service.AttrServiceInfo;

@Service
public class AttrServiceImpl implements AttrServiceInfo {
	@Autowired
	AttrMapper attrMapper;

	@Override
	public List<OBJECT_T_MALL_ATTR> get_list_attr_byclass_2_id(int class_2_id) {

		List<OBJECT_T_MALL_ATTR> list_attr = attrMapper.select_list_attr_byclass_2_id(class_2_id);
		return list_attr;
	}

	@Override
	public void save_attr(List<OBJECT_T_MALL_ATTR> list_attr, int class_2_id) {
		for (int i = 0; i < list_attr.size(); i++) {
			// 始终抓住双重集合概念；
			list_attr.get(i).setFlbh2(class_2_id);
			attrMapper.insert_attr(class_2_id, list_attr.get(i));
			attrMapper.insert_attr_value(list_attr.get(i).getId(), list_attr.get(i).getList_value());

		}
	}

	@Override
	public List<Integer> get_attr_id() {
		 List<Integer> list_attr_id=attrMapper.select_attr_id();
		return list_attr_id;
	}

	@Override
	public List<Integer> get_value_id(Integer attr_id) {
		List<Integer> list_value_id=attrMapper.select_value_id(attr_id);
		return list_value_id;
	}

}
