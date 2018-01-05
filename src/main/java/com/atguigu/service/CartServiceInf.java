package com.atguigu.service;

import java.util.List;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;
import com.atguigu.bean.T_MALL_USER_ACCOUNT;

public interface CartServiceInf {

	void add_cart(T_MALL_SHOPPINGCAR cart);

	void update_cart(T_MALL_SHOPPINGCAR t_MALL_SHOPPINGCAR);

	List<T_MALL_SHOPPINGCAR> get_list_cart_db_by_userid(T_MALL_USER_ACCOUNT login);

//	void changeShfxz(T_MALL_SHOPPINGCAR t_MALL_SHOPPINGCAR);

}
