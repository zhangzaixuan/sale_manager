package com.atguigu.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.bean.OBJECT_T_MALL_FLOW;
import com.atguigu.bean.OBJECT_T_MALL_ORDER;
import com.atguigu.bean.T_MALL_ADDRESS;
import com.atguigu.bean.T_MALL_ORDER_INFO;
import com.atguigu.exception.OverSaleException;
import com.atguigu.mapper.OrderMapper;
import com.atguigu.service.OrderServiceInf;
import com.atguigu.util.MyDateUtil;
import com.atguigu.util.MyJsonUtil;

@Service
public class OrderServiceImp implements OrderServiceInf {
	@Autowired
	OrderMapper orderMapper;

	@Override
	public void save_order(BigDecimal sum, T_MALL_ADDRESS address, OBJECT_T_MALL_ORDER order) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("address", address);
		map.put("order", order);
		orderMapper.insert_order(map);

		List<OBJECT_T_MALL_FLOW> list_flow = order.getList_flow();

		for (int i = 0; i < list_flow.size(); i++) {
			list_flow.get(i).setDd_id(order.getId());
			list_flow.get(i).setMdd(address.getYh_dz());

			Map<Object, Object> map1 = new HashMap<Object, Object>();
			map1.put("flow", list_flow.get(i));
			map1.put("dd_id", order.getId());
			
			orderMapper.insert_flow(map1);
			// 好好看一下字段；

			List<T_MALL_ORDER_INFO> list_info = list_flow.get(i).getList_orderinf();
			Map<Object, Object> map2 = new HashMap<Object, Object>();
			map2.put("flow_id", list_flow.get(i).getId());
			map2.put("list_info", list_info);
			map2.put("dd_id", order.getId());
			orderMapper.insert_info(map2);

			List<Integer> list_gwch_id = new ArrayList<Integer>();
			for (int j = 0; j < list_info.size(); j++) {
				int gwch_id = list_info.get(j).getGwch_id();

				list_gwch_id.add(gwch_id);
			}
			orderMapper.delete_list_cart(list_gwch_id);
		}

	}
	

	@Override
	public void pay_order(OBJECT_T_MALL_ORDER order) throws OverSaleException {
		order.setYjsdshj(MyDateUtil.getMydate(3));
		orderMapper.update_order(order);
		List<OBJECT_T_MALL_FLOW> list_flow = order.getList_flow();
		for (int i = 0;i<list_flow.size();i++) {
			OBJECT_T_MALL_FLOW flow = list_flow.get(i);
			flow.setPsshj(MyDateUtil.getMydate(1));
			flow.setYwy("大魔王");
			flow.setPsmsh("猛禽已出笼");
			flow.setLxfsh("99999999666");
			orderMapper.update_flow(flow);

			List<T_MALL_ORDER_INFO> list_orderinf = list_flow.get(i).getList_orderinf();

			for (int j = 0; j < list_orderinf.size(); j++) {
				T_MALL_ORDER_INFO order_INFO = list_orderinf.get(j);
				long kc = get_kc(order_INFO.getSku_id());
				if (kc >= order_INFO.getSku_shl()) {
					orderMapper.update_kc(order_INFO);
				} else {
					throw new OverSaleException("oversale");
				}

			}

		}

	}

	private long get_kc(int sku_id) {

		long kc = orderMapper.select_kc_for_update(sku_id);

		return kc;
	}

}
