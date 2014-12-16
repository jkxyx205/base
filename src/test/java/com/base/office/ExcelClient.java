package com.base.office;

import java.io.File;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;

import com.rick.base.office.excel.ExcelCell;
import com.rick.base.office.excel.ExcelWorkbook;

public class ExcelClient {
	public static void main(String[] args) {
		ExcelWorkbook ex = new ExcelWorkbook();
		HSSFCellStyle s1 = ex.createStyle();
		s1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		s1.setFillForegroundColor(HSSFColor.LIME.index);
		s1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		s1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		s1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		s1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		HSSFSheet sheet = ex.createSheet("000");
		
		ex.createCell(sheet, new ExcelCell(0,1,"aaaaa").setHeightInPoints(25).setStyle(s1).setWidth(2).setHight(6));
		
 
		//输出
		File file = new File("F://test.xls");
		try {
			ex.write(file);
			
			Runtime.getRuntime().exec("cmd /k start F:\\test.xls");	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
