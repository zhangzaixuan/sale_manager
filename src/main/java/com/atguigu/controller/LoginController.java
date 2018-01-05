package com.atguigu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;
import com.atguigu.bean.T_MALL_USER_ACCOUNT;
import com.atguigu.mapper.LoginMapper;
import com.atguigu.server.ServerInf;
import com.atguigu.server.UserLoginServer;
import com.atguigu.service.CartServiceInf;
import com.atguigu.util.MyJsonUtil;
import com.atguigu.util.MyuploadUtils;

@Controller
public class LoginController {
	@Autowired
	LoginMapper loginMapper;
	@Autowired
	CartServiceInf cartServiceInf;

	@Autowired
	ServerInf serverinf;

	@Autowired
	UserLoginServer userLoginServer;

	// @Autowired
	// JmsTemplate jmsTemplate;
	// @Autowired
	// Queue queueDestination;

	@RequestMapping("login")
	public String login(String dataSource_type,
			@CookieValue(value = "list_cart_cookies", required = false) String list_cart_cookies,
			HttpServletResponse response, HttpSession session, T_MALL_USER_ACCOUNT user) {
		// T_MALL_USER_ACCOUNT login = loginMapper.select_user(user);
		T_MALL_USER_ACCOUNT login = null;
//		String ping = serverinf.ping(user);

		try {

			if (dataSource_type.equals("d1")) {

				login = userLoginServer.login(user);

			} else if (dataSource_type.equals("d2")) {
				login = userLoginServer.login_test(user);
			}

		} catch (Exception e) {
			e.getMessage();
			return "redirect:/goto_login.do";
		}

		if (login == null) {
			return "redirect:goto_login.do";
		} else {
			session.setAttribute("user", login);
			// Cookie cookie = new Cookie("yh_nch", user.getYh_nch());
			Cookie cookie = new Cookie("yh_nch", login.getYh_nch());
			cookie.setMaxAge(60 * 60 * 24);
			response.addCookie(cookie);

			Cookie cookie2 = null;
			try {
				cookie2 = new Cookie("yh_nch2", URLEncoder.encode("如花", "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.addCookie(cookie2);

			List<T_MALL_SHOPPINGCAR> list_cart_db = cartServiceInf.get_list_cart_db_by_userid(login);

			List<T_MALL_SHOPPINGCAR> list_cart_login_cookie = MyJsonUtil.json_to_list(list_cart_cookies,
					T_MALL_SHOPPINGCAR.class);

			unit(session, response, list_cart_db, list_cart_login_cookie);

			return "redirect:index.do";
		}
	}

	private void unit(HttpSession session, HttpServletResponse response, List<T_MALL_SHOPPINGCAR> list_cart_db,
			List<T_MALL_SHOPPINGCAR> list_cart_login_cookie) {

		T_MALL_USER_ACCOUNT login = (T_MALL_USER_ACCOUNT) session.getAttribute("user");

		// list_cart_db!=null||list_cart_db.size()>0

		if (list_cart_db == null || list_cart_db.size() == 0) {
			if (list_cart_login_cookie != null && list_cart_login_cookie.size() > 0) {
				for (int i = 0; i < list_cart_login_cookie.size(); i++) {
					cartServiceInf.add_cart(list_cart_login_cookie.get(i));
				}
			}
		} else {

			if (list_cart_login_cookie != null && list_cart_login_cookie.size() > 0) {

				for (int i = 0; i < list_cart_login_cookie.size(); i++) {

					boolean b = if_new_cart(list_cart_db, list_cart_login_cookie.get(i));

					if (b) {

						list_cart_login_cookie.get(i).setYh_id(login.getId());
						cartServiceInf.add_cart(list_cart_login_cookie.get(i));
					} else {
						T_MALL_SHOPPINGCAR cart = new T_MALL_SHOPPINGCAR();

						for (int j = 0; j < list_cart_db.size(); j++) {
							if (list_cart_db.get(j).getSku_id() == list_cart_login_cookie.get(i).getSku_id()) {
								cart = list_cart_db.get(j);
							}
						}
						list_cart_login_cookie.get(i).setId(cart.getId());

						list_cart_login_cookie.get(i).setTjshl(cart.getTjshl());
						list_cart_login_cookie.get(i).setSku_id(cart.getSku_id());
						// list_cart_login_cookie.get(i).setHj(cart.getTjshl()*cart.getTjshl());
						// 这里有个bug db的价格等信息及时更新；
						list_cart_login_cookie.get(i).setHj(
								list_cart_login_cookie.get(i).getTjshl() * list_cart_login_cookie.get(i).getSku_jg());
						cartServiceInf.update_cart(list_cart_login_cookie.get(i));
					}

				}

			}

		}
		// 清空和重设session 都需要操作；

		response.addCookie(new Cookie("list_cart_cookies", ""));

		session.setAttribute("list_cart_sessions", cartServiceInf.get_list_cart_db_by_userid(login));

	}

	@RequestMapping("goto_login")
	public String goto_login() {
		return "sale_login";
	}

	@RequestMapping("login_out")
	public String logout(HttpSession session) {
		session.invalidate();
		// session 立即失效
		return "redirect:/goto_login.do";
	}
	/*
	 * @RequestMapping("login") public String login(HttpServletResponse response,
	 * HttpSession session, T_MALL_USER_ACCOUNT login) {
	 * 
	 * if (login == null) { return "redirect:goto_login.do"; } else {
	 * T_MALL_USER_ACCOUNT user = loginMapper.select_user(login); Cookie cookie =
	 * new Cookie("yh_nch", user.getYh_nch());
	 * 
	 * cookie.setMaxAge(60 * 60 * 24); response.addCookie(cookie); return
	 * "redirect:index.do"; } }
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
}
