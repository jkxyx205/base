package com.rick.base.context.tld;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.rick.base.dictionary.service.DictionaryUtils;

class SelectUtil {
	static String selectString(String name,String key,boolean multiple) {
		Map<String,String> option = DictionaryUtils.getItemByKeyorAlias(key);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"form-control\" ").append("id").append("=\"").append(name).append("\"");
		
		if(multiple)
			sb.append(" multiple=\"multiple\""); //don't need name attribute
		else 
			sb.append("name").append("=\"").append(name).append("\"");
		   
			
		sb.append("\">");
		
		if(!multiple) 
			sb.append("<option value= \"\"></option>");
		Set<Entry<String,String>> set = option.entrySet();
		for(Entry<String,String> en : set) {
			sb.append("<option value=\"").append(en.getKey()).append("\">").append(en.getValue()).append("</option>");
		}
		sb.append("</select>");
		
		return sb.toString();
	}
}
