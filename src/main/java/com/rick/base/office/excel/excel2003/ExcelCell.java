package com.rick.base.office.excel.excel2003;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

 
public class ExcelCell {
	private int x;

	private int y;

	private int width = 1;

	private int hight = 1;

	private float heightInPoints = 12.75f;

	private HSSFCellStyle style;
	
	public HSSFCellStyle getStyle() {
		return style;
	}

	public ExcelCell setStyle(HSSFCellStyle style) {
		this.style = style;
		return this;
	}

	public float getHeightInPoints() {
		return heightInPoints;
	}

	public ExcelCell setHeightInPoints(float heightInPoints) {
		this.heightInPoints = heightInPoints;
		return this;
	}

	private String value;

	public ExcelCell(int x, int y, String value) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
	}

	public String getValue() {
		if (value == null || "null".equals(value))
			value = "";
		return value;
	}

	public ExcelCell setValue(String value) {
		this.value = value;
		return this;
	}

	public int getX() {
		return x;
	}

	public ExcelCell setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public ExcelCell setY(int y) {
		this.y = y;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public ExcelCell setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHight() {
		return hight;
	}

	public ExcelCell setHight(int hight) {
		this.hight = hight;
		return this;
	}
}
