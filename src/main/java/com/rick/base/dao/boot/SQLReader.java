package com.rick.base.dao.boot;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * User: Rick.Xu
 * Date: 15-1-28
 * Time: 上午11:04
 */
public class SQLReader {
	
	private static final transient Logger logger = LoggerFactory
			.getLogger(SQLReader.class);
	
    private static final Map<String,QuerySQL> sqlMap = new HashMap<String,QuerySQL>();
    
    public static String getSQLbyName(String name) throws Exception {
    	QuerySQL qs = sqlMap.get(name);
    	if(qs == null) {
    		logger.warn("query name [" + name + "] can not find sql accrodingly!");
    		return "";
    	}
    		
    	return qs.getSql();
    }
    
    static void getAllSQL(List<String> classpaths) throws Exception{
    	for (String classpath: classpaths) {
    		getAllSQL(classpath);
    	}
    }

    @SuppressWarnings("unchecked")
	public static void getAllSQL(String classpath) throws Exception{
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources;
        try {
            resources = resolver.getResources(classpath);
        } catch (IOException e) {
            e.printStackTrace();
            throw  e;
        }

        int len = resources.length;
        if(len < 1) {
        	return;
        }

        //handle

        for(Resource resource : resources) {
        	SAXReader reader = new SAXReader();
            Document document = reader.read(resource.getInputStream());
            Element root = document.getRootElement();
            Iterator<Element> it = root.elementIterator("sql-query");
            while(it.hasNext()) {
            	 Element e = it.next();
            	 QuerySQL qs = new QuerySQL();
                 qs.setName(e.attributeValue("name"));
                 qs.setSql(e.getTextTrim());
                 sqlMap.put(qs.getName(),qs);
            }
        }
    }
}
