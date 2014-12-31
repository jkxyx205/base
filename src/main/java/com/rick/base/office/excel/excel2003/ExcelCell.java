package com.rick.base.office.excel.excel2003;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import com.rick.base.office.excel.Builder;

 
public class ExcelCell {
	private final int x;

	private final int y;
	
	private final String value;

	private final int width;

	private final int height;

	private final float heightInPoints;

	private final HSSFCellStyle style;
	
	public HSSFCellStyle getStyle() {
		return style;
	}

	 
	public float getHeightInPoints() {
		return heightInPoints;
	}

	public String getValue() {
		return value;
	}

	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static class ExcelCellBuilder implements Builder<ExcelCell> {
		
		private int x;

		private int y;
		
		private String value;

		private int width = 1;

		private int height = 1;

		private float heightInPoints = 12.75f;

		private HSSFCellStyle style;
		
		public ExcelCellBuilder(int x, int y, String value) {
			this.x = x;
			this.y = y;
			this.value = value;
		}

		
		public ExcelCellBuilder width(int width) {
			this.width = width;
			return this;
		}
		
		public ExcelCellBuilder height(int height) {
			this.height = height;
			return this;
		}
		
		public ExcelCellBuilder heightInPoints(float heightInPoints) {
			this.heightInPoints = heightInPoints;
			return this;
		}
		
		public ExcelCellBuilder style(HSSFCellStyle style) {
			this.style = style;
			return this;
		}
		
		public ExcelCell build() {
			return new ExcelCell(this);
		}
		
	}
	private ExcelCell(ExcelCellBuilder builder) {
		if(StringUtils.isBlank(builder.value) || "null".equals(builder.value))
			builder.value = "";
		this.value = builder.value;
		this.x = builder.x;
		this.y = builder.y;
		
		this.width = builder.width;
		this.heightInPoints = builder.heightInPoints;
		this.style = builder.style;
		this.height = builder.height;
	}
}
