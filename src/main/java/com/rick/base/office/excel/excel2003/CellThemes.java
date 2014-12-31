package com.rick.base.office.excel.excel2003;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class CellThemes {
	private HSSFCellStyle s0;
	private HSSFCellStyle s1;
	private HSSFCellStyle s2;
	private HSSFCellStyle s3;
	private HSSFCellStyle s4;
	
	public HSSFCellStyle getS0() {
		return s0;
	}

	public HSSFCellStyle getS1() {
		return s1;
	}

	public HSSFCellStyle getS2() {
		return s2;
	}

	public HSSFCellStyle getS3() {
		return s3;
	}

	public HSSFCellStyle getS4() {
		return s4;
	}

	public CellThemes(HSSFWorkbook book) {
		init(book);
	}

	private void init(HSSFWorkbook book) {
		//Font 
		HSSFFont font = book.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLACK.index);
		
		HSSFFont font2 = book.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font2.setColor(HSSFColor.WHITE.index);
		
		//Color
		HSSFPalette customPalette = book.getCustomPalette();
		customPalette.setColorAtIndex(HSSFColor.PALE_BLUE.index, (byte) 0, (byte) 176, (byte) 240);
		customPalette.setColorAtIndex(HSSFColor.LIGHT_TURQUOISE.index, (byte) 218, (byte) 238, (byte) 243);
		
		//合并样式
		s0 = book.createCellStyle();
		//
		s1 = book.createCellStyle();
		s1.setFont(font);
		s1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		//
		s2 = book.createCellStyle();
		s2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		s2.setFillForegroundColor(HSSFColor.YELLOW.index);
		s2.setFont(font);
		s2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		//
		s3 = book.createCellStyle();
		s3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		s3.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		s3.setFont(font2);
		//
		s4 = book.createCellStyle();
		s4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		s4.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		font.setColor(HSSFColor.BLACK.index);
		s4.setFont(font);
	}
	
  
}
