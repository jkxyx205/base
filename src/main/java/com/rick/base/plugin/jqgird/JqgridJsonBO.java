package com.rick.base.plugin.jqgird;

import java.util.List;
import java.util.Map;
 
class JqgridJsonBO {
	private List<Map<String,Object>> rows;
	private long records;
	private long total;
	private int page;
	public List<Map<String, Object>> getRows() {
		return rows;
	}
	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}
	public long getRecords() {
		return records;
	}
	public void setRecords(long records) {
		this.records = records;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}

