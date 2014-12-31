package com.rick.base.office.excel.excel2007;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellThemes {
	private XSSFCellStyle s0;
	private XSSFCellStyle s1;
	private XSSFCellStyle s2;
	private XSSFCellStyle s3;
	private XSSFCellStyle s4;
	
	public XSSFCellStyle getS0() {
		return s0;
	}

	public XSSFCellStyle getS1() {
		return s1;
	}

	public XSSFCellStyle getS2() {
		return s2;
	}

	public XSSFCellStyle getS3() {
		return s3;
	}

	public XSSFCellStyle getS4() {
		return s4;
	}

	public CellThemes(XSSFWorkbook book) {
		init(book);
	}

	private void init(XSSFWorkbook book) {
		
		/*XSSFPalette customPalette = book.getCustomPalette();
		customPalette.setColorAtIndex(HSSFColor.PALE_BLUE.index, (byte) 0, (byte) 176, (byte) 240);
		customPalette.setColorAtIndex(HSSFColor.LIGHT_TURQUOISE.index, (byte) 218, (byte) 238, (byte) 243);*/
		
		//Font 
		XSSFFont font = book.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLACK.index);
		
		XSSFFont font2 = book.createFont();
		font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font2.setColor(HSSFColor.WHITE.index);
		
		//Color
		//XSSFColor color =  new XSSFColor(new java.awt.Color(0, 176, 240));
		XSSFColor color2 =  new XSSFColor(new java.awt.Color(218, 238, 243));
		
		//合并样式
		s0 = book.createCellStyle();
		//
		s1 = book.createCellStyle();
		s1.setFont(font);
		s1.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		//
		s2 = book.createCellStyle();
		s2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		s2.setFillForegroundColor(HSSFColor.YELLOW.index);
		s2.setFont(font);
		s2.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		//
		s3 = book.createCellStyle();
		s3.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		s3.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		s3.setFont(font2);
		//
		s4 = book.createCellStyle();
		s4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		s4.setFillForegroundColor(color2);
		font.setColor(HSSFColor.BLACK.index);
		s4.setFont(font);
	}
	
  
}
