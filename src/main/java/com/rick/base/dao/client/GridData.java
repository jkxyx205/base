package com.rick.base.dao.client;

import java.util.List;
import java.util.Map;

public class GridData {
	
	private long total;
	
	private List<Map<String,Object>> dataList;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}
}
