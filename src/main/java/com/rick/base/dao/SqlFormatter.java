package com.rick.base.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Rick.Xu
 *
 */
class SqlFormatter {
	
	private static final transient Logger logger = LoggerFactory.getLogger(SqlFormatter.class);
	
	private static final String PARAM_REGEX = "(?i)\\s*(=|<|>|like|!=|>=|<=|in)\\s*[(]?:\\w+[)]?";
	   
	private static final String PARAM_REPLACE = "(((?i)[a-zA-Z0-9[\\.]_[-]]+)|([(][^([(]|[)])]*[)]))\\s*(?i)(=|<|>|like|!=|>=|<=)\\s*:";
	
	private static final String IN_PARAM_REPLACE = "(((?i)[a-zA-Z0-9[\\.]_[-]]+)|([(][^([(]|[)])]*[)]))\\s*(?i)(in)\\s*[(]\\s*:";
	
	private static final Map<String,String> DATE_FORMAT_MAP;
	
	static {
		DATE_FORMAT_MAP = new HashMap<String,String>(2);
		DATE_FORMAT_MAP.put("\\d{4}/\\d{2}/\\d{2}", "yyyy/MM/dd");
		DATE_FORMAT_MAP.put("\\d{4}-\\d{2}-\\d{2}", "yyyy-MM-dd");
	}
	
	@SuppressWarnings("unchecked")
	static String formatSql(String srcSql,Map<String,Object> param,Map<String, Object> formatMap,String paramInSeperator) {
		if(formatMap == null || param == null) {
			formatMap = Collections.EMPTY_MAP; 
		} else {
			Set<Entry<String,Object>> set = param.entrySet();
			
			for(Entry<String,Object> en : set) {
				String name = en.getKey();
				Object obj = en.getValue();
				
				String value = "";
				
				if(obj.getClass() == String[].class) {
					String[] values = (String[])obj;
					if(values.length > 0) {
						value = values[0];
					}
				} else {
					value = String.valueOf(obj);
				}
	
				if(StringUtils.isNotBlank(value) && srcSql.matches(new StringBuilder().append(".*").append(IN_PARAM_REPLACE).append(name).append("\\s*[)]").append(".*").toString())) {
					String[] invalues = null;
					if ((invalues = value.split(paramInSeperator)).length > 0) {
						StringBuilder sb = new StringBuilder("in (");
						for(int i = 0; i< invalues.length; i++) {
							String newProName = name + i;
							sb.append(":").append(newProName).append(",");
							formatMap.put(newProName, invalues[i]);
						}
						sb.deleteCharAt(sb.length()-1);
						sb.append(")");
						sb.toString();
						srcSql = srcSql.replaceAll("((?i)in)\\s*[(]\\s*:" + name + "\\s*[)]", sb.toString());
					}
				}
				formatMap.put(name, value);
			}
		}
		Pattern pat = Pattern.compile(PARAM_REGEX);  
		Matcher mat = pat.matcher(srcSql);  
		while (mat.find()) {
			 String matchRet = mat.group().trim();
			 
			 int end = matchRet.indexOf(")");
			 end = (end == -1 ? matchRet.length() : end);
			 
	         String placeHolder =  matchRet.substring(matchRet.indexOf(":")+1,end);
	         
	        String value = (String)formatMap.get(placeHolder);
			if (StringUtils.isNotBlank(value)){
				String oper = matchRet.split("\\s*:")[0];
				
				String format;
				
				if("like".equalsIgnoreCase(oper)) {
				 	 value = new StringBuilder("%").append(value).append("%").toString();
		        	 formatMap.put(placeHolder, value);
		        	 
		        	 
				} else if(("<>=<=".indexOf(oper) > -1|| matchRet.equals("!=")) && StringUtils.isNotBlank(format = matchDate(value))) {
					try {
						formatMap.put(placeHolder, new SimpleDateFormat(format).parse(value));
					} catch (ParseException e) {
						 logger.debug(e.getMessage());
					}
				}
	       
	         }
	         
	      if(StringUtils.isBlank(value)) {
	    	  srcSql = srcSql.replaceAll(PARAM_REPLACE + placeHolder, "1 = 1").replaceAll(IN_PARAM_REPLACE +placeHolder +"\\s*[)]" , "1 = 1");
	      }
		}
		
		return srcSql;
	}
	
	static String formatSqlCount(String srcSql) {
		srcSql = srcSql.replaceAll("(?i)order\\s+by\\s+(\\S+)\\s+(desc|asc)?","");
		StringBuilder sb = new  StringBuilder();
		sb.append("SELECT COUNT(*) FROM (").append(srcSql).append(")");
		return sb.toString();
	}
	
	
	private static String matchDate(String value) {
		Set<String> formats = DATE_FORMAT_MAP.keySet();
		for(String f : formats) {
			if(value.matches(f))
				return DATE_FORMAT_MAP.get(f);
		}
		return null;
	}
}
