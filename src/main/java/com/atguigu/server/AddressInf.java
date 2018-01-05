package com.atguigu.server;

import java.util.List;

import javax.jws.WebService;

import com.atguigu.bean.T_MALL_ADDRESS;
import com.atguigu.bean.T_MALL_USER_ACCOUNT;

@WebService
public interface AddressInf {
	
	void insert_address(T_MALL_ADDRESS adress);
	
	List<T_MALL_ADDRESS>get_address_by_user(T_MALL_USER_ACCOUNT  user_id);
	
	T_MALL_ADDRESS get_address_by_addressid(int address_id);

}
