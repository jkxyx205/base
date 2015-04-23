package com.rick.base.test;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rick.base.test.bean.User;
import com.rick.base.test.service.TestService;
import com.rick.base.util.I18nUtil;

@Controller
public class TestController {
	private static final transient Logger logger = LoggerFactory.getLogger(TestController.class);  
	
	@Resource
	private TestService service;

	@RequestMapping(value="/index")
	public String gotoIndex(HttpServletRequest req) throws Exception {
		logger.debug("add1 & add1 execute...");
		System.out.println(I18nUtil.getMessageByCode("username",new Object[] {"/hello"}));
		
		service.query();
		
		//throw new BusinessException();
		return "index";
	}
	
	@RequestMapping(value="/testi18n")
	public String gotoi18n() {
		return "test/i18n";
	}
	
	@RequestMapping(value="/testJqgrid")
	public String gotoJqgird() {
		return "test/jqgird";
	}
	
	@RequestMapping(value="/bootstrap")
	public String gotoBootstrap() {
		return "test/bootstrap";
	}
	
	@RequestMapping(value="/login")
	public String gotoLogin() {
		return "login";
	}
	
	@RequestMapping(value="/register")
	public String gotoRegister(Model model) {
		//Model model request范围的属性
		
		model.addAttribute("name", "myname");
		
		User user = new User();
		user.setName("张三");
		model.addAttribute("user", user);
		return "test/register";
	}
	
	@RequestMapping(value="/quartz")
	public String gotoQuartz() {
		return "quartz/quartz";
	}
	
	@RequestMapping(value="/message")
	@ResponseBody
	public String message(@RequestBody List<User> users) {
		return "{'status':'success'}";
	}
	
	
}
