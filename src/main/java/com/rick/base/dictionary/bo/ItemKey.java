package com.rick.base.dictionary.bo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ItemKey {
	/** 
	 * @field multilevel
	 * 是否是多级字典
	*/
	private boolean multilevel;
	
	/** 
	 * @field map
	 *	key：value 存储值
	 *	value: 显示值
	 * 
	*/
	private Map<String,String> itemMap = new LinkedHashMap<String, String>();
	
	/** 
	 * @field alias
	 * 别名
	*/
	private String alias;
	
	/** 
	 * @field itemList
	 * 常用于jstl初始化combo控件
	 * 协助解析多级字典
	*/
	private List<DicItem> itemList = new ArrayList<DicItem>();

	public boolean isMultilevel() {
		return multilevel;
	}

	public void setMultilevel(boolean multilevel) {
		this.multilevel = multilevel;
	}

	public Map<String, String> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, String> itemMap) {
		this.itemMap = itemMap;
	}

	public List<DicItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<DicItem> itemList) {
		this.itemList = itemList;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
