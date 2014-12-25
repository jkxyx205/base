package com.rick.base.office.excel.excel2007;

import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *  Various utility functions that make working with a cells and rows easier.  The various
 * methods that deal with style's allow you to create your XSSFCellStyles as you need them.
 * When you apply a style change to a cell, the code will attempt to see if a style already
 * exists that meets your needs.  If not, then it will create a new style.  This is to prevent
 * creating too many styles.  there is an upper limit in Excel on the number of styles that
 * can be supported.
 */
public final class XSSFCellUtil {

    private XSSFCellUtil() {
        // no instances of this class
    }

    /**
     *  Get a row from the spreadsheet, and create it if it doesn't exist.
     *
     *@param  rowIndex  The 0 based row number
     *@param  sheet       The sheet that the row is part of.
     *@return             The row indicated by the rowCounter
     */
    public static XSSFRow getRow(int rowIndex, XSSFSheet sheet) {
    	return (XSSFRow) CellUtil.getRow(rowIndex, sheet);
    }

    /**
     * Get a specific cell from a row. If the cell doesn't exist,
     *  then create it.
     *
     *@param  row     The row that the cell is part of
     *@param  columnIndex  The column index that the cell is in.
     *@return         The cell indicated by the column.
     */
    public static XSSFCell getCell(XSSFRow row, int columnIndex) {
        return (XSSFCell) CellUtil.getCell(row, columnIndex);
    }

    /**
     *  Creates a cell, gives it a value, and applies a style if provided
     *
     * @param  row     the row to create the cell in
     * @param  column  the column index to create the cell in
     * @param  value   The value of the cell
     * @param  style   If the style is not null, then set
     * @return         A new XSSFCell
     */
    public static XSSFCell createCell(XSSFRow row, int column, String value, XSSFCellStyle style) {
    	return (XSSFCell) CellUtil.createCell(row, column, value, style);
    }

    /**
     *  Create a cell, and give it a value.
     *
     *@param  row     the row to create the cell in
     *@param  column  the column index to create the cell in
     *@param  value   The value of the cell
     *@return         A new XSSFCell.
     */
    public static XSSFCell createCell(XSSFRow row, int column, String value) {
        return createCell( row, column, value, null );
    }

    /**
     *  Take a cell, and align it.
     *
     *@param  cell     the cell to set the alignment for
     *@param  workbook               The workbook that is being worked with.
     *@param  align  the column alignment to use.
     *
     * @see XSSFCellStyle for alignment options
     */
    public static void setAlignment(XSSFCell cell, XSSFWorkbook workbook, short align) {
    	CellUtil.setAlignment(cell, workbook, align);
    }

    /**
     *  Take a cell, and apply a font to it
     *
     *@param  cell     the cell to set the alignment for
     *@param  workbook               The workbook that is being worked with.
     *@param  font  The XSSFFont that you want to set...
     */
    public static void setFont(XSSFCell cell, XSSFWorkbook workbook, XSSFFont font) {
    	CellUtil.setFont(cell, workbook, font);
    }

    /**
     *  This method attempt to find an already existing XSSFCellStyle that matches
     *  what you want the style to be. If it does not find the style, then it
     *  creates a new one. If it does create a new one, then it applies the
     *  propertyName and propertyValue to the style. This is necessary because
     *  Excel has an upper limit on the number of Styles that it supports.
     *
     *@param  workbook               The workbook that is being worked with.
     *@param  propertyName           The name of the property that is to be
     *      changed.
     *@param  propertyValue          The value of the property that is to be
     *      changed.
     *@param  cell                   The cell that needs it's style changes
     */
    public static void setCellStyleProperty(XSSFCell cell, XSSFWorkbook workbook,
			String propertyName, Object propertyValue) {
    	CellUtil.setCellStyleProperty(cell, workbook, propertyName, propertyValue);
    }

    /**
     *  Looks for text in the cell that should be unicode, like &alpha; and provides the
     *  unicode version of it.
     *
     *@param  cell  The cell to check for unicode values
     *@return       translated to unicode
     */
    public static XSSFCell translateUnicodeValues(XSSFCell cell){
    	CellUtil.translateUnicodeValues(cell);
    	return cell;
    }
}