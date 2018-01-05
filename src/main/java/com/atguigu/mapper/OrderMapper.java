package com.atguigu.mapper;

import java.util.List;
import java.util.Map;

import com.atguigu.bean.OBJECT_T_MALL_FLOW;
import com.atguigu.bean.OBJECT_T_MALL_ORDER;
import com.atguigu.bean.T_MALL_ORDER_INFO;

public interface OrderMapper {

	void insert_order(Map<Object, Object> map);

	void insert_flow(Map<Object, Object> map1);

	void insert_info(Map<Object, Object> map2);

	void delete_list_cart(List<Integer> list);
	
	
	

	long select_kc_for_update(int sku_id);

	void update_flow(OBJECT_T_MALL_FLOW flow);

	void update_order(OBJECT_T_MALL_ORDER order);

	void update_kc(T_MALL_ORDER_INFO order_INFO);

	
	
//	list 和 collection 中 "list"

}
