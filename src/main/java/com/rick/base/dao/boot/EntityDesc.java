package com.rick.base.dao.boot;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GenerationType;

public class EntityDesc {
	private String tableName;
	
	/**
	 * 主键
	 */
	private String primaryKey;
	
	/**
	 * 主键策略
	 */
	private GenerationType type;
	

	private List<Column> column = new ArrayList<Column>();
	

	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getPrimaryKey() {
		return primaryKey;
	}


	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}


	public List<Column> getColumn() {
		return column;
	}


	public void setColumn(List<Column> column) {
		this.column = column;
	}

	public GenerationType getType() {
		return type;
	}


	public void setType(GenerationType type) {
		this.type = type;
	}

	public static class Column {
		
		/**
		 * 数据库字段名
		 */
		private String dbColumnName;
		
		/**
		 * 属性名
		 */
		private String clazzProName;
		
		/**
		 * 数据库类型
		 */
		private String dbColumnType;
		
		/**
		 * 属性类型
		 */
		private Class<?> clazzProType;

		public String getDbColumnName() {
			return dbColumnName;
		}

		public void setDbColumnName(String dbColumnName) {
			this.dbColumnName = dbColumnName;
		}

		public String getClazzProName() {
			return clazzProName;
		}

		public void setClazzProName(String clazzProName) {
			this.clazzProName = clazzProName;
		}

		public String getDbColumnType() {
			return dbColumnType;
		}

		public void setDbColumnType(String dbColumnType) {
			this.dbColumnType = dbColumnType;
		}

		public Class<?> getClazzProType() {
			return clazzProType;
		}

		public void setClazzProType(Class<?> clazzProType) {
			this.clazzProType = clazzProType;
		}
	}

}
