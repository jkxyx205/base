package com.base.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.dao.BaseDaoImpl.JdbcTemplateExecutor;
import com.rick.base.office.excel.excel2007.ExcelCell;
import com.rick.base.office.excel.excel2007.ExcelWorkbook;
import com.rick.base.test.bean.SmScenarioVO;
import com.rick.base.test.report.ReportService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "classpath:system/applicationContext-*.xml","classpath:business/applicationContext-*.xml" }) 
public class ReportTest extends AbstractJUnit4SpringContextTests {
	@Resource
	ReportService service;
	
	@Resource(name = "baseDao")
	private BaseDaoImpl dao;
	
	@Test
	public void testReport() throws FileNotFoundException, Exception {
		
		/*List<String[]> comments = new ArrayList<String[]>();
		comments.add(new String[] {"Promotion Meter Report"});
		comments.add(new String[] {"Report Period:","2014/11/17","2014/11/18"});
		comments.add(new String[] {"Report generated on:","2014/11/17"});
		 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("STORE_CODE", "57");
		
		Map<String,String> head = new HashMap<String,String>();
		head.put("STORE_CODE", "商店 Code");
		
		service.Report("testReport", map, head, comments,"f:\\aa.xls");*/
		
		Map<String,Object> param = new HashMap<String,Object>();
		//param.put("ID", "1419328510900");
		param.put("STORE_CODE", "3333;3204");
		param.put("CREATE_DATE", "2014-11-12");
		
		List<SmScenarioVO> list9 = dao.queryForSpecificParam("querySmscenario4", param, new JdbcTemplateExecutor<List<SmScenarioVO>>() {
			public  List<SmScenarioVO> query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
				return jdbcTemplate.query(queryString, args,new RowMapper<SmScenarioVO>() {

					public SmScenarioVO mapRow(ResultSet rs, int arg1)
							throws SQLException {
						SmScenarioVO vo = new SmScenarioVO();
						vo.setId(rs.getString(1));
						return vo;
					}
					
				});
			}
		});
		
		System.out.println("list9---------------------" +list9);
		
	}
	
	@Test
	public void testExcelWriterCell2007() {
		ExcelWorkbook ex = new ExcelWorkbook();
		XSSFCellStyle s1 = ex.createStyle();
		s1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		s1.setFillForegroundColor(HSSFColor.BLUE.index);
		s1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		s1.setBorderRight(XSSFCellStyle.BORDER_THIN);
		s1.setBorderTop(XSSFCellStyle.BORDER_THIN);
		s1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		
		XSSFSheet sheet = ex.createSheet("000");
		
		ex.createCell(sheet, new ExcelCell(1,1,"0.4").setHeightInPoints(25).setStyle(s1).setWidth(2).setHight(6));
		
		ex.createCell(sheet, new ExcelCell(5,5,"0.5").setHeightInPoints(25).setWidth(6).setHight(2));
		
 
		//输出
		File file = new File("F://test.xlsx");
		try {
			ex.write(file);
			
			Runtime.getRuntime().exec("cmd /k start F:\\test.xlsx");	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
