package com.rick.base.test.bean;


public class SmScenarioVO {
	private String id;
	
	private String storeCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	
	@Override
	public String toString() {
		return "id: " + id + ",storeCode:" + storeCode;
	}
}
