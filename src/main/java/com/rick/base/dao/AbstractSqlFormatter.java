package com.rick.base.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public abstract class AbstractSqlFormatter {
	
	private static final transient Logger logger = LoggerFactory.getLogger(AbstractSqlFormatter.class);
	
//	private static final String COLUNM_REGEX = "[a-zA-Z0-9[\\.]_[-]]+";
	//add column function
	private static final String COLUNM_REGEX = "((?i)(to_char|NVL)?\\s*([(][^([(]|[)])]*[)])|[a-zA-Z0-9'[\\.]_[-]]+)";
	
	private static final String OPER_REGEX = "(?i)(like|!=|>=|<=|<|>|=|\\s+in|\\s+not\\s+in)";
	//private static final String HOLDER_REGEX = "[(\\s*]?:\\w+[\\s*)]?";
	private static final String HOLDER_REGEX = "(([(\\s*]:\\w+[\\s*)])|(:\\w+))";
	
	private static final String PARAM_REGEX = ":\\w+";
	private static final String FULL_REGEX = new StringBuilder().append(COLUNM_REGEX).append("\\s*").append(OPER_REGEX).append("\\s*").append(HOLDER_REGEX).toString();
	
	private static final Map<String,String> DATE_FORMAT_MAP;
	
	static {
		DATE_FORMAT_MAP = new HashMap<String,String>(2);
		DATE_FORMAT_MAP.put("\\d{4}/\\d{2}/\\d{2}", "yyyy/MM/dd");
		DATE_FORMAT_MAP.put("\\d{4}-\\d{2}-\\d{2}", "yyyy-MM-dd");
	}
	
	String formatSql(String srcSql,Map<String,Object> param,Map<String, Object> formatMap,String paramInSeperator) {
		if(formatMap == null || param == null) {
			return srcSql.replaceAll(FULL_REGEX, "1 = 1");
		} else {
			List<ParamHolder> paramList = splitParam(srcSql);
			
			for(ParamHolder h : paramList) {
				String name = h.holder;
				Object obj = param.get(name);
				
				obj = (obj == null ? "":obj);
				String value = null;
				
				if(obj.getClass() == String[].class) {
					String[] values = (String[])obj;
					if(values.length > 0) {
						StringBuilder sb = new StringBuilder();
						for(String s : values) {
							sb.append(s).append(";");
						}
						value = sb.toString();
					}
				} else {
					value = String.valueOf(obj);
				}
				
				if(StringUtils.isBlank(value)) {
					srcSql = srcSql.replace(h.full, "1 = 1");
					continue;
				}
				
				//if has the value
				String format;
				if(h.oper.toUpperCase().endsWith("IN")) {
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
						srcSql = srcSql.replaceAll("((?i)in)\\s*[(]\\s*:" + h.holder + "\\s*[)]", sb.toString());
					}
					
				} else if("like".equalsIgnoreCase(h.oper)) {
		        	 srcSql = srcSql.replace(h.full, new StringBuilder("UPPER(").append(h.key).append(") ").append(h.oper).append(" '%'||UPPER(:").append(name).append(")||'%'"));
		        	 formatMap.put(name, value);
				} else if(("<>=<=".indexOf(h.oper) > -1|| h.oper.equals("!=")) && StringUtils.isNotBlank(format = matchDate(value))) {
					try {
						if(!formatMap.containsKey(name))
							formatMap.put(name, new SimpleDateFormat(format).parse(value));
					} catch (ParseException e) {
						 logger.debug(e.getMessage());
					}
				} else {
					formatMap.put(name, value);
				}
				
			}
		}
		return srcSql;
	}
	
	public String formatSqlCount(String srcSql) {
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
	
	private static List<ParamHolder> splitParam(String sql) {
		Pattern pat = Pattern.compile(FULL_REGEX);  
		Matcher mat = pat.matcher(sql);  
		List<ParamHolder> paramList = new ArrayList<ParamHolder>();
		
		while (mat.find()) {
			 ParamHolder holder = new ParamHolder();
			 String matchRet = mat.group().trim();
			 holder.full = matchRet;
			 //鍐嶈繘琛屾媶鍒�
			 Pattern pat1 = Pattern.compile("^" + COLUNM_REGEX);  
			 Matcher mat1 = pat1.matcher(matchRet);
			 while(mat1.find()) {
				 String matchRet1 = mat1.group().trim();
				 holder.key = matchRet1;
			 }
			 
			 Pattern pat2 = Pattern.compile(OPER_REGEX);  
			 Matcher mat2 = pat2.matcher(matchRet);
			 while(mat2.find()) {
				 String matchRet2 = mat2.group().trim();
				 holder.oper = matchRet2;
			 }
			 
			 
			//鍐嶈繘琛屾媶鍒�
			 Pattern pat3 = Pattern.compile(PARAM_REGEX);  
			 Matcher mat3 = pat3.matcher(matchRet);
			 while(mat3.find()) {
				 String matchRet3 = mat3.group().trim();
				 holder.holder = matchRet3.substring(1);
			 }
			 
			 paramList.add(holder);
		}
		logger.debug(paramList.toString());
		return paramList;
	}
	
   private static class ParamHolder {
		private String full;
		
		private String key;
		
		private String oper;
		
		private String holder;
		
		@Override
		public String toString() {
			return new StringBuilder().append(full).append("/").append(key).append("/").append(oper).append("/").append(holder).toString();
		}
	}
   
}
