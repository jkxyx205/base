package com.rick.base.plugin.jqgird;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Rick.Xu
 *
 */
@Controller
public class JqgridController {
	@Resource
	JqgridService service;
	
	@RequestMapping("/jqrid")
	@ResponseBody
	public JqgridJsonBO jqgridJsonData(HttpServletRequest request) throws Exception {
		return service.getResponse(request);
	}
	
	@RequestMapping("/jqrid/export")
	public void jqridExport(HttpServletRequest request,HttpServletResponse response) throws Exception {
		service.export(request, response);
	}
	
}

