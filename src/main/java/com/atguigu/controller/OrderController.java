package com.atguigu.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.atguigu.bean.OBJECT_T_MALL_FLOW;
import com.atguigu.bean.OBJECT_T_MALL_ORDER;
import com.atguigu.bean.T_MALL_ADDRESS;
import com.atguigu.bean.T_MALL_ORDER;
import com.atguigu.bean.T_MALL_ORDER_INFO;
import com.atguigu.bean.T_MALL_SHOPPINGCAR;
import com.atguigu.bean.T_MALL_USER_ACCOUNT;
import com.atguigu.exception.OverSaleException;
import com.atguigu.server.AddressInf;
import com.atguigu.service.CartServiceInf;
import com.atguigu.service.OrderServiceInf;
import com.atguigu.util.MyCartSumUtil;

@SessionAttributes("order")
@Controller
public class OrderController {

	@Autowired
	AddressInf addressInf;
	@Autowired
	OrderServiceInf orderInf;

	@Autowired
	CartServiceInf cartServiceInf;

	@RequestMapping("pay_success")
	public String pay_success(@ModelAttribute("order")OBJECT_T_MALL_ORDER order, BigDecimal zje) {

		return "sale_order_success";

	}

	// 作为一种结构搁在这里，不再调用；
	@RequestMapping("pay_fail/{message}")
	public String order_fail(@PathVariable("message") String message,
			@ModelAttribute("order") OBJECT_T_MALL_ORDER order, BigDecimal sum) {

		return "sale_order_fail";

	}

	@RequestMapping("order_pay")
	public String order_pay(@ModelAttribute("order")OBJECT_T_MALL_ORDER order, BigDecimal zje) {
		try {
			orderInf.pay_order(order);
			
		} catch (OverSaleException e) {
			
			return "redirect:/pay_fail/oversale.do";
			
		}
		
		return "redirect:/pay_success.do";

	}

	@RequestMapping("goto_cashier")
	public String goto_cashier(@ModelAttribute("order")OBJECT_T_MALL_ORDER order, BigDecimal zje) {

		return "sale_cashier";

	}

	@RequestMapping("save_order")
	public String save_order(@ModelAttribute("order") OBJECT_T_MALL_ORDER order, HttpSession session, BigDecimal zje,
			int address_id) {

		T_MALL_USER_ACCOUNT login = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		T_MALL_ADDRESS address = addressInf.get_address_by_addressid(address_id);

		orderInf.save_order(zje, address, order);

		// List<T_MALL_SHOPPINGCAR>
		// list_cart=cartServiceInf.get_list_cart_db_by_userid(login);

		session.setAttribute("list_cart_sessions", cartServiceInf.get_list_cart_db_by_userid(login));

		return "redirect:/goto_cashier.do";

	}

	@RequestMapping("goto_check_order")
	public String goto_check_order(BigDecimal zje, HttpSession session, ModelMap map) {
		T_MALL_USER_ACCOUNT login = (T_MALL_USER_ACCOUNT) session.getAttribute("user");

		List<T_MALL_SHOPPINGCAR> list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_sessions");
		// 设定订单信息；
		OBJECT_T_MALL_ORDER order = new OBJECT_T_MALL_ORDER();
		order.setJdh(0);
		order.setYh_id(login.getId());
		order.setZje(MyCartSumUtil.caleCartSum(list_cart));
		// 利用set的不可重复特性（set为不可重复无序存储容器）
		Set<String> set_kcdz = new HashSet<String>();
		for (int i = 0; i < list_cart.size(); i++) {
			if (list_cart.get(i).getShfxz().equals("1")) {
				set_kcdz.add(list_cart.get(i).getKcdz());
			}
		}
		List<OBJECT_T_MALL_FLOW> list_flow = new ArrayList<OBJECT_T_MALL_FLOW>();

		Iterator<String> iterator = set_kcdz.iterator();
		/**
		 * order （整单）根据kcdz拆单，变为为list_flow ，flow 中分配为T_MALL_ORDER_INFO;
		 */

		while (iterator.hasNext()) {
			// 获取库存地址；
			String kcdz = iterator.next();
			OBJECT_T_MALL_FLOW flow = new OBJECT_T_MALL_FLOW();
			flow.setPsfsh("天海战寄");// 配送方式；
			flow.setMqdd("开门放货");
			flow.setYh_id(login.getId());
			// flow.setYh_id(order.getYh_id());
			// 不是有OBJECT_T_MALL_FLOW 吗；
			List<T_MALL_ORDER_INFO> list_info = new ArrayList<T_MALL_ORDER_INFO>();

			for (int i = 0; i < list_cart.size(); i++) {
				if (list_cart.get(i).getShfxz().equals("1") && kcdz.equals(list_cart.get(i).getKcdz())) {
					T_MALL_SHOPPINGCAR cart = list_cart.get(i);
					T_MALL_ORDER_INFO info = new T_MALL_ORDER_INFO();
					// 生成订单中的商品信息
					info.setGwch_id(cart.getId());
					info.setShp_tp(cart.getShp_tp());
					info.setSku_id(cart.getSku_id());
					info.setSku_jg(cart.getSku_jg());
					info.setSku_kcdz(kcdz);
					info.setSku_mch(cart.getSku_mch());
					info.setSku_shl(cart.getTjshl());
					list_info.add(info);
				}
			}
			flow.setList_orderinf(list_info);
			//
			list_flow.add(flow);
		}
		order.setList_flow(list_flow);
		//
		map.put("order", order);
		map.put("list_address", addressInf.get_address_by_user(login));
		map.put("sum", MyCartSumUtil.caleCartSum(list_cart));
		return "sale_check_order";
	}

}
