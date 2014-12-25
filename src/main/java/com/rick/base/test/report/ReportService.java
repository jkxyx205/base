package com.rick.base.test.report;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.dao.BaseDaoImpl.JdbcTemplateExecutor;
import com.rick.base.office.excel.excel2003.ExcelCell;
import com.rick.base.office.excel.excel2003.ExcelRow;
import com.rick.base.office.excel.excel2003.ExcelWorkbook;

/**
 * @author Rick.Xu
 *
 */
@Service
public class ReportService {
	@Resource
	private BaseDaoImpl dao;
	
	private ExcelWorkbook ex;
	
	private ReportStyle defautlStyle;
	
	private int colunmLen;
	
	private static final int CELL_WIDTH = 6000;
	
	public ReportStyle getDefautlStyle() {
		return defautlStyle;
	}
	
	
	public void Report(String queryName,Map<String,String> header,List<String[]> comments,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> param = getMap(true,request);
		OutputStream os = getOsFromResponse(response,request,"test");
		Report(queryName,param,header,comments,os);
	}
	
	public void Report(String queryName,Map<String,Object> param,Map<String,String> header,List<String[]> comments,String filePath) throws FileNotFoundException, Exception {
		Report(queryName,param,header,comments,new FileOutputStream(filePath));
	}
	
	private OutputStream getOsFromResponse(HttpServletResponse response,HttpServletRequest request, String fileName) throws IOException {
		OutputStream os = null;
		String _fileName = fileName+".xls";
		if(request.getHeader("User-Agent").toLowerCase().indexOf("firefox") >0) { //火狐
			_fileName = new String(_fileName.getBytes("utf-8"), "ISO-8859-1");
		} else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") >0) { //IE
			_fileName = java.net.URLEncoder.encode(_fileName,"utf-8");
		}
        response.reset();// 清空输出流   
        response.setCharacterEncoding("utf-8");
		
        response.setHeader("Content-disposition", "attachment; filename="+_fileName+"");// 设定输出文件头   
        response.setContentType("application/vnd.ms-excel;charset=utf-8");// 定义输出类型 
        os = response.getOutputStream(); // 取得输出流   
		return os;
	}
	

	
	//public synchronized void Report(String queryName,Map<String,Object> param ,final Map<String,String> header,List<String[]> comments,WriteExcel write) {
	private synchronized void Report(String queryName,Map<String,Object> param ,final Map<String,String> header,List<String[]> comments,OutputStream os) throws Exception {
		final Map<String,String> title = new LinkedHashMap<String,String>();
		
		ex = new ExcelWorkbook();
		defautlStyle = new ReportStyle();
		
		final HSSFSheet sheet = ex.createSheet("Sheet0");
		
		int commentsSize = comments.size();
		//Write comments
		for(int i = 0; i < commentsSize; i++) {
			String[] comm = comments.get(i);
			if(comm != null) {
				int commLen = comm.length;
				if(commLen > 0) {
					if(i == 0) {
						ex.createCell(sheet, new ExcelCell(0, 0, comm[0]).setWidth(3).setHight(1).setStyle(defautlStyle.boldBlue()));
					} else {
						for(int j = 0; j < commLen; j++) {
							if(j == 0) {
								ex.createCell(sheet, new ExcelCell(j, i, comm[j]).setStyle(defautlStyle.boldBlack()));
							} else {
								ex.createCell(sheet, new ExcelCell(j, i, comm[j]).setStyle(defautlStyle.boldYellow()));
							}
						}
					}
				}
			}
		}
		
		//write table
		final int startIdx = comments.size()+1;
		dao.queryForSpecificParam(queryName, param, new JdbcTemplateExecutor<Serializable>() {

			public Serializable query(JdbcTemplate jdbcTemplate,
					String queryString, Object[] args) {
				dao.getJdbcTemplate().query(queryString, args, new RowCallbackHandler() {
					
					private boolean first;
					
					private int rows;
					
					public void processRow(ResultSet rs) throws SQLException {
						if(!first) {
							first = true;
							ResultSetMetaData rsmd = rs.getMetaData();
							colunmLen = rsmd.getColumnCount();
							
							for(int k = 1; k <=colunmLen; k++ ) {
								String label = rsmd.getColumnLabel(k);
								String htitle = null;
								if(header != null && StringUtils.isNotBlank(htitle = header.get(label))) {
									title.put(label, htitle);
								} else {
									title.put(label, label);
								}
							}
						}
						
						String[] values = new String[colunmLen];
						for(int i = 0; i < colunmLen; i++) {
							values[i] = rs.getString(i+1);
						}
						ExcelRow row = new ExcelRow(0, rows++ + startIdx + 1 ,values);
						ex.createRow(sheet, row);
						
					}
				});
				return null;
			}
			
		});

		//set column width
		int[] width = new int[colunmLen];
		for (int i = 0; i < colunmLen; i++)
			width[i] = CELL_WIDTH;
		ex.setColumnWidth(sheet, width);

		//write title
		String[] t = new String[colunmLen];
		ExcelRow headerRow = new ExcelRow(0, startIdx,title.values().toArray(t)).setStyle(defautlStyle.boldLightBlue());
		ex.createRow(sheet, headerRow);
		
		//customer to use
	/*	if(write != null)
			write.wrirte(ex,sheet);*/
		
		//output
		ex.write(os);
	}
	
/*	public interface WriteExcel {
		public void wrirte(ExcelWorkbook ex,HSSFSheet sheet);
		
	}*/
	
	public Map<String,Object> getMap(boolean skipBlink,HttpServletRequest request) {
		Enumeration<String> en = request.getParameterNames();
		Map<String, Object> map = null;
		boolean isfirst = true;
		while(en.hasMoreElements()) {
			if(isfirst)
				map = new HashMap<String, Object>();
			isfirst = false;
			String name = en.nextElement();
			String[] values = request.getParameterValues(name);
			if(values != null) {
					if(values.length>1) {
						   StringBuilder sb = new StringBuilder();
							for(String v:values) {
								if(skipBlink && StringUtils.isBlank(v))
									continue;
								sb.append(v).append(";");
							}
							sb.deleteCharAt(sb.length()-1);
					 
						map.put(name, sb.toString());
					} else  {
						String value = values[0];
						if(skipBlink && StringUtils.isBlank(value))
							continue;
						map.put(name,value);
					}
			} 
				
		}
		return map;
	}
	
	
	
	private class ReportStyle  {
	{
			HSSFPalette customPalette = ex.getBook().getCustomPalette();
			customPalette.setColorAtIndex(HSSFColor.PALE_BLUE.index, (byte) 0, (byte) 176, (byte) 240);
			customPalette.setColorAtIndex(HSSFColor.LIGHT_TURQUOISE.index, (byte) 218, (byte) 238, (byte) 243);
	}  
		/**
		 * 黑色字体 + 加粗
		 * @return
		 */
		public HSSFCellStyle boldBlack() {
			HSSFCellStyle s1 = ex.createStyle();
			HSSFFont font = ex.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			s1.setFont(font);
			return s1;
		}
		
		/**
		 * 黑色字体 + 加粗 +背景黄色
		 * @return
		 */
		public HSSFCellStyle boldYellow() {
			HSSFCellStyle s1 = ex.createStyle();
			s1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			s1.setFillForegroundColor(HSSFColor.YELLOW.index);
			HSSFFont font = ex.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			s1.setFont(font);
			return s1;
		}
		
		/**
		 * 白字体 + 加粗 +背景蓝色
		 * @return
		 */
		public HSSFCellStyle boldBlue() {
			HSSFCellStyle s1 = ex.createStyle();
			s1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			s1.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			HSSFFont font = ex.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFColor.WHITE.index);
			s1.setFont(font);
			return s1;
		}
		
		/**
		 * 黑色字体 + 加粗 +背景浅蓝色
		 * @return
		 */
		public HSSFCellStyle boldLightBlue() {
			HSSFCellStyle s1 = ex.createStyle();
			s1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			s1.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
			HSSFFont font = ex.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFColor.BLACK.index);
			s1.setFont(font);
			return s1;
		}
	}
}
