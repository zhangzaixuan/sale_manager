package com.atguigu.bean;

import java.io.Serializable;

public class T_MALL_SKU_CLASS extends T_MALL_SKU implements Serializable{
	
	private T_MALL_PRODUCT spu;
    
	private T_MALL_TRADE_MARK tm;

	public T_MALL_PRODUCT getSpu() {
		return spu;
	}

	public void setSpu(T_MALL_PRODUCT spu) {
		this.spu = spu;
	}

	public T_MALL_TRADE_MARK getTm() {
		return tm;
	}

	public void setTm(T_MALL_TRADE_MARK tm) {
		this.tm = tm;
	}
	
	
}
