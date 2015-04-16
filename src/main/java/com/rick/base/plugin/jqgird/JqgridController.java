package com.rick.base.plugin.jqgird;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rick.base.context.ResponseMsg;
import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.util.ServletContextUtil;

/**
 * @author Rick.Xu
 *
 */
@Controller
public class JqgridController {
	@Resource
	private JqgridService service;
	
	@Resource
	private BaseDaoImpl dao;
	
	
	
	@RequestMapping(value="/jqrid",method=RequestMethod.POST)
	@ResponseBody
	public JqgridJsonBO jqgridJsonData(HttpServletRequest request) throws Exception {
		return service.getResponse(request);
	}
	
	@RequestMapping("/jqrid/export")
	public void jqridExport(HttpServletRequest request,HttpServletResponse response) throws Exception {
		service.export(request, response);
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public ResponseMsg update(HttpServletRequest request) throws Exception {
		
		int count = 0;
		
		ResponseMsg msg = new ResponseMsg();
		try {
			Map<String,Object> param = ServletContextUtil.getMap(true, request);
			count = dao.executeForSpecificParam((String)param.get("queryName"), param);
			msg.setStatus(true);
			msg.setData(count);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return msg;
	}
	
	
}

