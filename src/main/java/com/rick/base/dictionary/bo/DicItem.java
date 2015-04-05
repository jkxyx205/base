package com.rick.base.dictionary.bo;

import com.rick.base.util.I18nUtil;

/** 
 * <description> 
 * 对应表：zyk_gg_zdxb
 * @author  xuxu
 * @datetime  Sep 17, 2013 2:05:29 PM
*/
public class DicItem {
	/** 
	 * @field id
	 * 编号
	*/
	private String id;
	
	/** 
	 * @field key
	 * 字典分类编号
	*/
	private String key;
	
	/** 
	 * @field dm
	 * 存储值
	*/
	private String value;
	
	/** 
	 * @field dmwb
	 * 显示值
	*/
	private String trueValue;
	
	/** 
	 * @field sjdmbh
	 * 如果是多级字典，上级字典bh
	*/
	private String fid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(String trueValue) {
		this.trueValue = trueValue;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	
	  
}
