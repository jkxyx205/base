package com.rick.base.test;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String gotoRegister() {
		return "test/register";
	}
	
	
	
	
}
