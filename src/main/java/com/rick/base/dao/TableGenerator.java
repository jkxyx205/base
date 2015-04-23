package com.rick.base.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rick.base.dao.boot.EntityDesc;
import com.rick.base.dao.boot.EntityReader;

public class TableGenerator {
	private static final transient Logger logger = LoggerFactory
			.getLogger(TableGenerator.class);
	
	@Resource(name = "baseDao")
	private BaseDaoImpl dao;
	
	public void createTable(Class<?> clazz) {
		EntityDesc ed = EntityReader.getEntityDesc(clazz);
		if (null == ed) {
			throw new RuntimeException("no entity class " +  clazz);
		}
		
		
		StringBuilder sb = new StringBuilder("CREATE TABLE ");
		sb.append(ed.getTableName()).append("(");
		
		for (EntityDesc.Column c : ed.getColumn()) {
			sb.append(c.getDbColumnName()).append(" ").append(getDbType(c));
			if(!c.isNullable()) {
				sb.append(" not null");
			} 
			
			if(c.isUnique()) {
				sb.append(" unique");
			} 
			
			if(c.getDbColumnName().equals(ed.getPrimaryKey()))
				sb.append(" primary key");
			
			sb.append(",");
			
		}
		sb.deleteCharAt(sb.length()-1);
		
		sb.append(")");
		
		logger.debug(sb.toString());
		
		dao.getJdbcTemplate().execute(sb.toString());
	}
	
	private String getDbType(EntityDesc.Column c) {
		if(StringUtils.isNotBlank(c.getColumnDefinition()))
			return c.getColumnDefinition();
		
		if(c.getClazzProType() == String.class) 
			return "VARCHAR2(" + c.getLength() + ")";
		else if(c.getClazzProType() == Integer.class || c.getClazzProType() == Long.class) {
			if (c.getPrecision() == 0) {
				return "NUMBER";
			} else {
				return "NUMBER("+c.getPrecision()+")";
			}
			
		}
		else if(c.getClazzProType() == Float.class || c.getClazzProType() == Double.class) {
			if (c.getPrecision() == 0) {
				return "NUMBER";
			} else {
				return "NUMBER("+c.getPrecision()+","+c.getScale()+")";
			}
		} else if(c.getClazzProType() == Date.class || c.getClazzProType() == java.sql.Date.class) {
			return "Date";
		} else if(c.getClazzProType() == Character.class) {
			return "CHAR(1)";
		} else {
			return "VARCHAR2(" + c.getLength() + ")";
		}
	}
}
