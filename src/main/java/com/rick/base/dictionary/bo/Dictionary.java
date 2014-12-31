package com.rick.base.dictionary.bo;

import java.util.List;
import java.util.Map;
/**
 * dictionary.xml
 * <description> 
 * @author  xuxu
 * @datetime  Jun 8, 2013 1:02:06 PM
 */
public class Dictionary {
	List<Item> itemList;
	
	private String standardSql;
 
	
	/**
	 * (item 和 area) 合并成类 Item.class
	 * <description> 
	 * @author  xuxu
	 * @datetime  Jun 8, 2013 1:00:55 PM
	 */
	public class Item {
		private String description;
		
		private String key;
		
		private String alias;
		
		private List<String> patterns;
		
		private Map<String,String> map;
		

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public List<String> getPatterns() {
			return patterns;
		}

		public void setPatterns(List<String> patterns) {
			this.patterns = patterns;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public Map<String, String> getMap() {
			return map;
		}

		public void setMap(Map<String, String> map) {
			this.map = map;
		}
	}
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	public String getStandardSql() {
		return standardSql;
	}
	public void setStandardSql(String standardSql) {
		this.standardSql = standardSql;
	}
}
