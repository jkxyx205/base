package com.rick.base.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;


/**
 * @author Rick.Xu
 * @version 
 * @since JDK 1.6
 */
public class ServletContextUtil {
	public static OutputStream getOsFromResponse(HttpServletResponse response,HttpServletRequest request, String fileName) throws IOException {
		OutputStream os = null;
		String _fileName = fileName.replaceAll("[\\/:*?\"<>[|]]", "");
		
		String browserType = request.getHeader("User-Agent").toLowerCase();
		
		if(browserType.indexOf("firefox") > -1) { //FF
			_fileName = "=?UTF-8?B?"+(new String(Base64.encodeBase64(_fileName.getBytes("UTF-8"))))+"?=";
		} else {
			if(fileName.matches(".*[^\\x00-\\xff]+.*")) {
				if(request.getHeader("User-Agent").toLowerCase().indexOf("msie") > -1) { //IE
					_fileName = java.net.URLEncoder.encode(_fileName,"utf-8");
				} else  { //其他
					_fileName = new String(_fileName.getBytes("utf-8"), "ISO-8859-1");
				}
			}
		}
 
        response.reset();// 清空输出流   
        response.setCharacterEncoding("utf-8");
		
        response.setHeader("Content-disposition", "attachment; filename="+_fileName+"");// 设定输出文件头   
        response.setContentType("application/vnd.ms-excel;charset=utf-8");// 定义输出类型 
        os = response.getOutputStream(); // 取得输出流   
		return os;
	}
	
	public static Map<String,Object> getMap(boolean skipBlink,HttpServletRequest request) {
		Enumeration<String> en = request.getParameterNames();
		Map<String, Object> map = null;
		boolean isfirst = true;
		while(en.hasMoreElements()) {
			if(isfirst)
				map = new HashMap<String, Object>();
			isfirst = false;
			String name = en.nextElement();
			String[] values = request.getParameterValues(name);
			if(values != null) {
					if(values.length>1) {
						   StringBuilder sb = new StringBuilder();
							for(String v:values) {
								if(skipBlink && StringUtils.isBlank(v))
									continue;
								sb.append(v).append(";");
							}
							sb.deleteCharAt(sb.length()-1);
					 
						map.put(name, sb.toString());
					} else  {
						String value = values[0];
						if(skipBlink && StringUtils.isBlank(value))
							continue;
						map.put(name,value);
					}
			} 
		}
		return map;
	}
}
