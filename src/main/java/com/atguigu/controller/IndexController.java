package com.atguigu.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	
	/*@RequestMapping("index")
	public String index() {
		return "sale_index";
	}*/
//	关于用户帐号信息尽可能不要在服务器和浏览器端传输
	@RequestMapping("index")
	public String index(HttpServletRequest request,ModelMap map) {
		return "sale_index";
	}
}
