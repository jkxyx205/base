package com.rick.base.office.excel;

public interface ExcelResultSet {
	public boolean rowMapper(int index,String[] value) throws Exception;
}
