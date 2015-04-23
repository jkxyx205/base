package com.rick.base.office.excel.excel2007;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rick.base.office.excel.ExcelResultSet;

/**
 * 
 * @author Rick.Xu
 * @Date 2014-12-16
 *
 */
public class ExcelReader {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final transient Logger logger = LoggerFactory.getLogger(ExcelReader.class);  

	/*public static int sheetNumbers(InputStream is) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(is);
		return wb.getNumberOfSheets();
	}*/
	
	
	/**
	 * @param is
	 * @param ers
	 * @return 总的行数
	 * @throws Exception
	 */
	public static void readExcelContent(InputStream is,ExcelResultSet ers) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFSheet sheet;
		XSSFRow row;
		
		int sheetIndexs = wb.getNumberOfSheets();
		
		for (int sheetIndex = 0; sheetIndex < sheetIndexs; sheetIndex++) {
			sheet = wb.getSheetAt(sheetIndex);
	        // 得到总行数
	        int rowNum = sheet.getLastRowNum();
	        row = sheet.getRow(0);
	        String sheetName = sheet.getSheetName();
	        int colNum = row.getPhysicalNumberOfCells();
	        // 正文
	        for (int i = 0; i <= rowNum; i++) {
	            row = sheet.getRow(i);
	            if(row == null)
	            	continue;
	            
	            int j = 0;
	            String[] str = new String[colNum];
	            while (j < colNum) {
	            	str[j] = getCellFormatValue(row.getCell(j)).trim();
	                j++;
	            }
	            if(logger.isDebugEnabled()) {
	            	StringBuilder rowData = new StringBuilder();
	            	for(String s : str) {
	            		rowData.append(s).append(" ");
	            	}
	            	logger.debug(rowData.toString());
	            }
	            
	            if(!ers.rowMapper(i, str,sheetIndex,sheetName)) {
	            	break;
	            }
	        }
		 }
		ers.afterReader();
    }
  
	/**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private static String getCellFormatValue(XSSFCell cell) {
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
}
