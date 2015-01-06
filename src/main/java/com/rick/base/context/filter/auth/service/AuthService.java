package com.rick.base.context.filter.auth.service;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.rick.base.dao.BaseDaoImpl;

@Service
public class AuthService {
	private static final String AUTH_CODE = "authId";
	
	@Resource
	private BaseDaoImpl dao;
	
	public String html(String srcHtml) {
		Document doc = Jsoup.parse(srcHtml);
		
		Elements elements = doc.getElementsByAttribute(AUTH_CODE);
		for(Element element : elements) {
			String authId = element.attr(AUTH_CODE);
			System.out.println("---------------------" + authId);
			
			//if no promossion delete element
			if(authId == AUTH_CODE) {
				element.remove();
			}
		}
		//取消转义
		return StringEscapeUtils.unescapeHtml(doc.outerHtml());
	}
}
