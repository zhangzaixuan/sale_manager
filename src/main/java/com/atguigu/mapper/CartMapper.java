package com.atguigu.mapper;

import java.util.List;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;
import com.atguigu.bean.T_MALL_USER_ACCOUNT;

public interface CartMapper {

	void insert_cart(T_MALL_SHOPPINGCAR cart);

	void update_cart(T_MALL_SHOPPINGCAR t_MALL_SHOPPINGCAR);

	List<T_MALL_SHOPPINGCAR> select_list_cart_db_by_userid(T_MALL_USER_ACCOUNT login);

//	void updateShfxz(T_MALL_SHOPPINGCAR t_MALL_SHOPPINGCAR);

	
	
}
