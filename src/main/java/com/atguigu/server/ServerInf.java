package com.atguigu.server;

import javax.jws.WebService;

import com.atguigu.bean.T_MALL_USER_ACCOUNT;

@WebService
public interface ServerInf {
	
	
	
	public String ping(T_MALL_USER_ACCOUNT hello);

}
