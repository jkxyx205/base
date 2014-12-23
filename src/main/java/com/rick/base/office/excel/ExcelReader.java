package com.rick.base.office.excel;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Rick.Xu
 * @Date 2014-12-16
 *
 */
public class ExcelReader {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final transient Logger logger = LoggerFactory.getLogger(ExcelReader.class);  

	public static enum ExcelVersion {
		V2003,V2007;
	}
	
	public static void readExcelContent(InputStream is,ExcelVersion version,ExcelResultSet ers) throws Exception {
		readExcelContent(is,version,ers,0);
    }
	
	public static int readExcelContent(InputStream is,ExcelVersion version,ExcelResultSet ers,int sheetIndex) throws Exception {
		int rowNum = 0;
		if(version == ExcelVersion.V2003)
			rowNum = readExcelContent2003(is,ers,sheetIndex);
		else if(version == ExcelVersion.V2007)
			rowNum = readExcelContent2007(is,ers,sheetIndex);
		
		return rowNum;
	}
	
	
	private static int readExcelContent2007(InputStream is,ExcelResultSet ers,int sheetIndex) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFSheet sheet;
		XSSFRow row;
		
        sheet = wb.getSheetAt(sheetIndex);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文
        for (int i = 0; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            String[] str = new String[colNum];
            while (j < colNum) {
            	str[j] = getCellFormatValue2007(row.getCell(j)).trim();
                j++;
            }
            if(logger.isDebugEnabled()) {
            	StringBuilder rowData = new StringBuilder();
            	for(String s : str) {
            		rowData.append(s).append(" ");
            	}
            	logger.debug(rowData.toString());
            }
            
            if(!ers.rowMapper(i, str)) {
            	break;
            }
        }
        return rowNum;
    }
  
   private static int readExcelContent2003(InputStream is,ExcelResultSet ers,int sheetIndex) throws Exception {
	HSSFWorkbook wb = new HSSFWorkbook(is);
	HSSFSheet sheet;
	HSSFRow row;
    
    sheet = wb.getSheetAt(sheetIndex);
    // 得到总行数
    int rowNum = sheet.getLastRowNum();
    row = sheet.getRow(0);
    int colNum = row.getPhysicalNumberOfCells();
    // 正文
    for (int i = 0; i <= rowNum; i++) {
        row = sheet.getRow(i);
        int j = 0;
        String[] str = new String[colNum];
        while (j < colNum) {
        	str[j] = getCellFormatValue2003(row.getCell(j)).trim();
            j++;
        }
        
        if(logger.isDebugEnabled()) {
        	StringBuilder rowData = new StringBuilder();
        	for(String s : str) {
        		rowData.append(s).append(" ");
        	}
        	logger.debug(rowData.toString());
        }
        if(!ers.rowMapper(i, str)) {
        	break;
        }
    }
    return rowNum;
}
  
	public static interface ExcelResultSet {
		public boolean rowMapper(int index,String[] value) throws Exception;
	}
	
	/**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private static String getCellFormatValue2007(XSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_NUMERIC:
            case XSSFCell.CELL_TYPE_FORMULA: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    cellvalue = sdf.format(date);
                    
                }
                else {
                    cellvalue = String.valueOf(new BigDecimal(cell.getNumericCellValue()).toString());
                }
                break;
            }
            case XSSFCell.CELL_TYPE_STRING:
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
    private static String getCellFormatValue2003(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    cellvalue = sdf.format(date);
                    
                }
                else {
                    cellvalue = String.valueOf(new BigDecimal(cell.getNumericCellValue()).toString());
                }
                break;
            }
            case HSSFCell.CELL_TYPE_STRING:
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
}
