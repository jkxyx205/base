package com.rick.base.office.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

 
public class ExcelRow {
	private int x;

	private int y;

	private float heightInPoints = 12.75f;

	private HSSFCellStyle style;
	
	private String[] values;
	
	public String[] getValues() {
		return values;
	}

	public ExcelRow setValues(String[] values) {
		this.values = values;
		return this;
	}

	public HSSFCellStyle getStyle() {
		return style;
	}

	public ExcelRow setStyle(HSSFCellStyle style) {
		this.style = style;
		return this;
	}

	public float getHeightInPoints() {
		return heightInPoints;
	}

	public ExcelRow setHeightInPoints(float heightInPoints) {
		this.heightInPoints = heightInPoints;
		return this;
	}


	public ExcelRow(int x, int y, String[] values) {
		super();
		this.x = x;
		this.y = y;
		this.values = values;
	}


	public int getX() {
		return x;
	}

	public ExcelRow setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public ExcelRow setY(int y) {
		this.y = y;
		return this;
	}

}
