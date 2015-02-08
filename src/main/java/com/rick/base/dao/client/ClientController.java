package com.rick.base.dao.client;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClientController {
	
	@Resource
	private ClientService service;

	@RequestMapping("/query/{queryName}")
	@ResponseBody
	public GridData getResultSet(@PathVariable String queryName,HttpServletRequest request) {
		return service.getResultSet(queryName, request);
	}
	
	@RequestMapping("/execute/{queryName}")
	@ResponseBody
	public String executeSql(@PathVariable String queryName,HttpServletRequest request) {
		try {
			service.executeSQL(queryName,request);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
		return "success";
	}
}
