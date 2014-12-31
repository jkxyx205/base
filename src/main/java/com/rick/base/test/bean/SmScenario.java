package com.rick.base.test.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "sm_scenario")
public class SmScenario {
	@Id
	private String id;
	
	@Column(name="STORE_CODE")
	private String storeCode;
	
	@Transient
	private String status;
	
	@Transient
	private String flag;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

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
		return "id: " + id + ",storeCode:" + storeCode + ",flag:" + flag + ",status:" + status;
	}
}
