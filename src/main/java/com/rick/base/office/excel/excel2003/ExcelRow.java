package com.rick.base.office.excel.excel2003;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import com.rick.base.office.excel.Builder;

 
public class ExcelRow {
	final private int x;

	final private int y;

	final private float heightInPoints;

	final private HSSFCellStyle style;
	
	final private String[] values;
	
	public String[] getValues() {
		return values;
	}

	public HSSFCellStyle getStyle() {
		return style;
	}

	public float getHeightInPoints() {
		return heightInPoints;
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public static class ExcelRowBuilder implements Builder<ExcelRow> {

		private int x;

		private int y;

		private float heightInPoints = 12.75f;

		private HSSFCellStyle style;
		
		private String[] values;
		
		public ExcelRowBuilder(int x, int y, String[] values) {
			this.x = x;
			this.y = y;
			this.values = values;
		}
		
		public ExcelRowBuilder heightInPoints(float heightInPoints) {
			this.heightInPoints = heightInPoints;
			return this;
		}
		
		public ExcelRowBuilder style(HSSFCellStyle style) {
			this.style = style;
			return this;
		}
		
		public ExcelRowBuilder values(String[] values) {
			this.values = values;
			return this;
		}
		
		
		public ExcelRow build() {
			return new ExcelRow(this);
		}
		
	}
	
	private ExcelRow(ExcelRowBuilder builder) {
		this.x = builder.x;
		this.y = builder.y;
		this.heightInPoints = builder.heightInPoints;
		this.values = builder.values;
		this.style = builder.style;
	}

}
