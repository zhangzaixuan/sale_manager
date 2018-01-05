package com.atguigu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.atguigu.bean.OBJECT_T_MALL_ATTR;
import com.atguigu.bean.T_MALL_VALUE;

public interface AttrMapper {

	List<OBJECT_T_MALL_ATTR> select_list_attr_byclass_2_id(int class_2_id);

	void insert_attr(@Param("class_2_id")int class_2_id, @Param("attr")OBJECT_T_MALL_ATTR object_T_MALL_ATTR);

	void insert_attr_value(@Param("attr_id")int id, @Param("list_value")List<T_MALL_VALUE> list_value);

	List<Integer> select_attr_id();

	List<Integer> select_value_id(Integer attr_id);

	


}
