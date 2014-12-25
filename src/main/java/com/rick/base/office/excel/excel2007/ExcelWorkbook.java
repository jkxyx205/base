package com.rick.base.office.excel.excel2007;

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

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Rick.Xu
 *
 */
public class ExcelWorkbook {
	private XSSFWorkbook book = new XSSFWorkbook();
	
	public XSSFWorkbook getBook() {
		return book;
	}

	private Map<XSSFSheet,Map<Integer,XSSFRow>> sheetList = new LinkedHashMap<XSSFSheet,Map<Integer,XSSFRow>>();
	
	private Map<XSSFSheet,List<CellRangeAddress>> border = new LinkedHashMap<XSSFSheet,List<CellRangeAddress>>();
	
	public XSSFCellStyle createStyle() {
		XSSFCellStyle style =  book.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
		style.setWrapText(true);
		return style;
	}
	
	
	public XSSFFont createFont() {
		return book.createFont();
	}
	
	public ExcelWorkbook() {
		
	}
	
	/**
	 * name can be dup
	 * @param name
	 * @return
	 */
	public XSSFSheet createSheet(String name) {
		XSSFSheet sheet = book.createSheet(name);
		return sheet;
	}
	
	/**
	 * 创建单元格
	 * @param ecell
	 */
	public void createCell(XSSFSheet sheet, ExcelCell ecell) {
		CellRangeAddress region = null;
		
		region = new CellRangeAddress(ecell.getY(), ecell.getY()+ecell.getHight()-1, ecell.getX(), ecell.getX()+  ecell.getWidth()-1);
		
		sheet.addMergedRegion(region);
		
		Map<Integer,XSSFRow> rowMap = sheetList.get(sheet);
		if(rowMap == null) {
			rowMap = new HashMap<Integer, XSSFRow>();
			sheetList.put(sheet, rowMap);
			
			List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
			border.put(sheet, list);
		}
		
		
		XSSFRow row = rowMap.get(ecell.getY());
		if(row == null) {
			row = sheet.createRow(ecell.getY());
			rowMap.put(ecell.getY(), row);
			row.setHeightInPoints(ecell.getHeightInPoints());
		} else {
			if(ecell.getHeightInPoints()> row.getHeightInPoints())
				row.setHeightInPoints(ecell.getHeightInPoints());
		}
		
		
		XSSFCell cell = row.createCell(ecell.getX());
		
		cell.setCellValue(ecell.getValue());
		
		XSSFCellStyle style = null;
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
	
	public void createRow(XSSFSheet sheet, ExcelRow row) {
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
	public void setColumnWidth(XSSFSheet sheet,int[] width) {
		for(int i = 0 ; i < width.length; i++) {
			sheet.setColumnWidth(i, width[i]);
		}
	}
	
	
	/**
	 * 设置边框
	 * @param region
	 */
	 private void setRegionStyle(XSSFSheet sheet,CellRangeAddress region) {
		//全部完成之后
		XSSFRow frow = XSSFCellUtil.getRow(region.getFirstRow(), sheet);
		XSSFCell fcell = XSSFCellUtil.getCell(frow, region.getFirstColumn());
		
		XSSFCellStyle style = fcell.getCellStyle();
		for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
			XSSFRow row = XSSFCellUtil.getRow(i, sheet);
			for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
				XSSFCell cell = XSSFCellUtil.getCell(row, j);
				cell.setCellStyle(style);
			}
		}
	 }
	 
	public void write(File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		write(fos);
	}
	
	public void write(OutputStream os) throws Exception {
		Set<Entry<XSSFSheet, List<CellRangeAddress>>> set = border.entrySet();
		Iterator<Entry<XSSFSheet, List<CellRangeAddress>>> it = set.iterator();
		while(it.hasNext()) {
			Entry<XSSFSheet, List<CellRangeAddress>> en = it.next();
			
			XSSFSheet sheet = en.getKey();
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
