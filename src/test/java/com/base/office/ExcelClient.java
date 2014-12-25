package com.base.office;

import java.io.File;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.junit.Test;

import com.rick.base.office.excel.excel2003.ExcelCell;
import com.rick.base.office.excel.excel2003.ExcelRow;
import com.rick.base.office.excel.excel2003.ExcelWorkbook;

public class ExcelClient {
	@Test
	public void testExcelWriteRow() {
		ExcelWorkbook ex = new ExcelWorkbook();
		HSSFCellStyle s1 = ex.createStyle();
		s1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		s1.setFillForegroundColor(HSSFColor.LIME.index);
		s1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		s1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		s1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		s1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		HSSFSheet sheet = ex.createSheet("222");
		
		
		ExcelRow row = new ExcelRow(1,1,new String[] {"0.4","col22"}).setStyle(s1);
		ExcelRow row2 = new ExcelRow(0,3,new String[] {"colx","colxx"});
		
		ex.createRow(sheet, row);
		ex.createRow(sheet, row2);
		
		//输出
		File file = new File("F://test.xls");
		try {
			ex.write(file);
			
			Runtime.getRuntime().exec("cmd /k start F:\\test.xls");	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExcelWriterCell() {
		ExcelWorkbook ex = new ExcelWorkbook();
		HSSFCellStyle s1 = ex.createStyle();
		s1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		s1.setFillForegroundColor(HSSFColor.LIME.index);
		s1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		s1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		s1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		s1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		HSSFSheet sheet = ex.createSheet("000");
		
		ex.createCell(sheet, new ExcelCell(1,1,"aaaaa").setHeightInPoints(25).setStyle(s1).setWidth(2).setHight(6));
		
		ex.createCell(sheet, new ExcelCell(5,5,"bbbbb").setHeightInPoints(25).setWidth(6).setHight(2));
		
 
		//输出
		File file = new File("F://test.xls");
		try {
			ex.write(file);
			
			Runtime.getRuntime().exec("cmd /k start F:\\test.xls");	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExcelWriterCell2007() {
		ExcelWorkbook ex = new ExcelWorkbook();
		HSSFCellStyle s1 = ex.createStyle();
		s1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		s1.setFillForegroundColor(HSSFColor.LIME.index);
		s1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		s1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		s1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		s1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		HSSFSheet sheet = ex.createSheet("000");
		
		ex.createCell(sheet, new ExcelCell(1,1,"aaaaa").setHeightInPoints(25).setStyle(s1).setWidth(2).setHight(6));
		
		ex.createCell(sheet, new ExcelCell(5,5,"bbbbb").setHeightInPoints(25).setWidth(6).setHight(2));
		
 
		//输出
		File file = new File("F://test.xlsx");
		try {
			ex.write(file);
			
			Runtime.getRuntime().exec("cmd /k start F:\\test.xls");	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void createI18iProperties() {
		/*String excelTemplate = "F:\\GitHub\\baseSys\\base\\src\\main\\webapp\\resources\\template\\i18n_properties.xlsx";
		try {
			I18nExcel2Properties.excel2Properties(excelTemplate, "F:\\properties_js", "F:\\properties_js\\aa.js");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
