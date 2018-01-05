package com.atguigu.bean;

import java.io.Serializable;
import java.util.List;

public class OBJECT_T_MALL_FLOW extends T_MALL_FLOW implements Serializable{
	
//	构建装配关系
	
	private List<T_MALL_ORDER_INFO> list_orderinf;

	public List<T_MALL_ORDER_INFO> getList_orderinf() {
		return list_orderinf;
	}

	public void setList_orderinf(List<T_MALL_ORDER_INFO> list_orderinf) {
		this.list_orderinf = list_orderinf;
	}

}
