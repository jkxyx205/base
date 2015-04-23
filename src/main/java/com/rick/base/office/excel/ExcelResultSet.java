package com.rick.base.office.excel;

public interface ExcelResultSet {
	public boolean rowMapper(int index,String[] value,int sheetIndex, String sheetName) throws Exception;
	
	public void afterReader();
}
