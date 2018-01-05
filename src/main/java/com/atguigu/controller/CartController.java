package com.atguigu.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;
import com.atguigu.bean.T_MALL_USER_ACCOUNT;
import com.atguigu.service.CartServiceInf;
import com.atguigu.util.MyJsonUtil;
import com.mysql.fabric.Response;

@Controller
public class CartController {

	@Autowired
	CartServiceInf cartServiceInf;

	@RequestMapping("change_status")
	public String changestatus(@CookieValue(value = "list_cart_cookies", required = false) String list_cart_cookies,
			HttpServletResponse reponse, T_MALL_SHOPPINGCAR cart, HttpSession session, ModelMap map) {
        
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

		if (user == null) {

			list_cart = MyJsonUtil.json_to_list(list_cart_cookies, T_MALL_SHOPPINGCAR.class);

		} else {

			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_sessions");

		}
		for (int i = 0; i < list_cart.size(); i++) {

			if (cart.getSku_id() == list_cart.get(i).getSku_id()) {
				list_cart.get(i).setShfxz(cart.getShfxz());

				if (user == null) {

					Cookie cookie = new Cookie("list_cart_cookies", MyJsonUtil.list_to_json(list_cart));
					cookie.setMaxAge(60 * 60 * 24);
					reponse.addCookie(cookie);

				} else {
					cartServiceInf.update_cart(list_cart.get(i));
				
					
					// 这边无需单独改变商品是否被选定，还是使用原有的update_cart 方法；
				}
			}

		}
		map.put("list_cart", list_cart);

		return "sale_cart_list_inner";

	}
	@RequestMapping("goto_minicart_list")
	public String goto_minicart_list(@CookieValue(value = "list_cart_cookies", required = false) String list_cart_cookies, HttpSession session,
			ModelMap map) {

		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

		if (user == null) {

			list_cart = MyJsonUtil.json_to_list(list_cart_cookies, T_MALL_SHOPPINGCAR.class);

		} else {

			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_sessions");

		}
		map.put("list_cart", list_cart);
		return "sale_cart_list";
	}

	@RequestMapping("minicart")
	public String miniCart(@CookieValue(value = "list_cart_cookies", required = false) String list_cart_cookies,
			HttpSession session, ModelMap map) {
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

		if (user == null) {

			list_cart = MyJsonUtil.json_to_list(list_cart_cookies, T_MALL_SHOPPINGCAR.class);

		} else {

			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_sessions");

		}
		
		map.put("list_cart", list_cart);
		return "sale_minicart_list";
	}

	@RequestMapping("add_cart")
	public String add_list_cart(@CookieValue(value = "list_cart_cookies", required = false) String list_cart_cookies,
			HttpServletResponse reponse, T_MALL_SHOPPINGCAR cart, HttpSession session, ModelMap map) {

		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

		// 如果用户未登录
		if (cart.getYh_id() == 0) {
			// list_cart_cookies==null&&list_cart.size()==0
			// 如果cookie为空
			if (StringUtils.isBlank(list_cart_cookies)) {
				list_cart.add(cart);
			} else {
				// 此处需要对cart进行判重
				list_cart = MyJsonUtil.json_to_list(list_cart_cookies, T_MALL_SHOPPINGCAR.class);
				boolean b = if_new_cart(list_cart, cart);
				if (b) {
					// 如果cookie_cart 与cart 不重复
					list_cart.add(cart);
				} else {
					for (int i = 0; i < list_cart.size(); i++) {
						if (list_cart.get(i).getSku_id() == cart.getSku_id()) {
							list_cart.get(i).setTjshl(list_cart.get(i).getTjshl() + cart.getTjshl());
							list_cart.get(i).setHj(list_cart.get(i).getTjshl() * (list_cart.get(i).getSku_jg()));
						}
					}
				}
			}
			//
			Cookie cookie = new Cookie("list_cart_cookies", MyJsonUtil.list_to_json(list_cart));
			cookie.setMaxAge(60 * 60 * 24);
			reponse.addCookie(cookie);
			// return "redirect:cart_success.do";
		}

		else {
			// 添加db_cart 和同步session;
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_sessions");

			if (list_cart == null || list_cart.size() == 0) {

				cartServiceInf.add_cart(cart);

				list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

				list_cart.add(cart);

				session.setAttribute("list_cart_sessions", list_cart);

			} else {
				// **注解还是加标定吧！

				boolean b = if_new_cart(list_cart, cart);

				if (b) {
					cartServiceInf.add_cart(cart);
					list_cart.add(cart);

				} else {
					for (int i = 0; i < list_cart.size(); i++) {
						if (list_cart.get(i).getSku_id() == cart.getSku_id()) {
							list_cart.get(i).setTjshl(list_cart.get(i).getTjshl() + cart.getTjshl());
							list_cart.get(i).setHj(list_cart.get(i).getTjshl() * (list_cart.get(i).getSku_jg()));
							cartServiceInf.update_cart(list_cart.get(i));
						}

					}

				}
			}
		}
		// 用户未登录的分支；
		return "redirect:cart_success.do";
	}

	/**
	 * list_cart and cart 有交集 b 为false ,需要对cart 进行update，负责需要add;
	 * 
	 * @param list_cart
	 * @param cart
	 * @return
	 */
	private boolean if_new_cart(List<T_MALL_SHOPPINGCAR> list_cart, T_MALL_SHOPPINGCAR cart) {
		boolean b = true;
		for (int i = 0; i < list_cart.size(); i++) {
			if (list_cart.get(i).getSku_id() == cart.getSku_id()) {
				b = false;
			}
		}
		return b;
	}

	

	@RequestMapping("cart_success")
	public String cart_success() {
		return "sale_cart_success";
	}

}
