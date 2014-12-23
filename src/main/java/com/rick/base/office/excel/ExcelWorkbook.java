package com.rick.base.office.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelWorkbook {
	private HSSFWorkbook book = new HSSFWorkbook();
	
	private Map<HSSFSheet,Map<Integer,HSSFRow>> sheetList = new LinkedHashMap<HSSFSheet,Map<Integer,HSSFRow>>();
	
	private Map<HSSFSheet,List<CellRangeAddress>> border = new LinkedHashMap<HSSFSheet,List<CellRangeAddress>>();
	
	public HSSFCellStyle createStyle() {
		HSSFCellStyle style =  book.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style.setWrapText(true);
		return style;
	}
	
	
	public HSSFFont createFont() {
		return book.createFont();
	}
	
	public ExcelWorkbook() {
		
	}
	
	/**
	 * name can be dup
	 * @param name
	 * @return
	 */
	public HSSFSheet createSheet(String name) {
		HSSFSheet sheet = book.createSheet(name);
		return sheet;
	}
	
	/**
	 * 创建单元格
	 * @param ecell
	 */
	public void createCell(HSSFSheet sheet, ExcelCell ecell) {
		CellRangeAddress region = null;
		
		region = new CellRangeAddress(ecell.getY(), ecell.getY()+ecell.getHight()-1, ecell.getX(), ecell.getX()+  ecell.getWidth()-1);
		
		sheet.addMergedRegion(region);
		
		Map<Integer,HSSFRow> rowMap = sheetList.get(sheet);
		if(rowMap == null) {
			rowMap = new HashMap<Integer, HSSFRow>();
			sheetList.put(sheet, rowMap);
			
			List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
			border.put(sheet, list);
		}
		
		
		HSSFRow row = rowMap.get(ecell.getY());
		if(row == null) {
			row = sheet.createRow(ecell.getY());
			rowMap.put(ecell.getY(), row);
			row.setHeightInPoints(ecell.getHeightInPoints());
		} else {
			if(ecell.getHeightInPoints()> row.getHeightInPoints())
				row.setHeightInPoints(ecell.getHeightInPoints());
		}
		
		
		HSSFCell cell = row.createCell(ecell.getX());
		
		cell.setCellValue(ecell.getValue());
		
		HSSFCellStyle style = null;
		if(ecell.getStyle() != null) {
			style = ecell.getStyle();
			cell.setCellStyle(style);
		} 
		
		//设置边框
		if(cell.getCellStyle().getBorderBottom() > 0 ) {
			List<CellRangeAddress> list = border.get(sheet);
			list.add(region);
		} 
	}
	
	public void createRow(HSSFSheet sheet, ExcelRow row) {
		String[] header = row.getValues();
		int len = header.length;
		for (int i = 0; i <len; i++) {
			ExcelCell ecell = new ExcelCell(row.getX()+i,row.getY(),header[i]).setHeightInPoints(row.getHeightInPoints()).setStyle(row.getStyle());
			createCell(sheet, ecell);
		}
	}
	
	/**
	 * 设置列的宽度
	 * @param width
	 */
	public void setColumnWidth(HSSFSheet sheet,int[] width) {
		for(int i = 0 ; i < width.length; i++) {
			sheet.setColumnWidth(i, width[i]);
		}
	}
	
	
	/**
	 * 设置边框
	 * @param region
	 */
	 private void setRegionStyle(HSSFSheet sheet,CellRangeAddress region) {
		//全部完成之后
		HSSFRow frow = HSSFCellUtil.getRow(region.getFirstRow(), sheet);
		HSSFCell fcell = HSSFCellUtil.getCell(frow, region.getFirstColumn());
		
		HSSFCellStyle style = fcell.getCellStyle();
		for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
			HSSFRow row = HSSFCellUtil.getRow(i, sheet);
			for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
				HSSFCell cell = HSSFCellUtil.getCell(row, j);
				cell.setCellStyle(style);
			}
		}
	 }
	 
	public void write(File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		write(fos);
	}
	
	public void write(OutputStream os) throws Exception {
		Set<Entry<HSSFSheet, List<CellRangeAddress>>> set = border.entrySet();
		Iterator<Entry<HSSFSheet, List<CellRangeAddress>>> it = set.iterator();
		while(it.hasNext()) {
			Entry<HSSFSheet, List<CellRangeAddress>> en = it.next();
			
			HSSFSheet sheet = en.getKey();
			List<CellRangeAddress> list = en.getValue();
			
			for(CellRangeAddress cr :list) {
				setRegionStyle(sheet, cr);
			}
		}
		
		book.write(os);
		os.flush();
		os.close();
	}
}
