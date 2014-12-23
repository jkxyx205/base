package com.rick.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rick.base.office.excel.ExcelReader;
import com.rick.base.office.excel.ExcelReader.ExcelResultSet;
import com.rick.base.office.excel.ExcelReader.ExcelVersion;
import com.spreada.utils.chinese.ZHConverter;

/**
 * @author Rick.Xu
 *
 */
@Component("I18nExcel2Properties")
public final class I18nExcel2Properties {
	
	private static final transient Logger logger = LoggerFactory.getLogger(I18nExcel2Properties.class);
	
	@Value("${excelTemplate}")
	private String  excelTemplate;
	
	@Value("${propertiesFolder}")
	private String  propertiesFolder;
	
	@Value("${jsFile}")
	private String  jsFile;
	
	@Value("${startCreateNew}")
	private String startCreateNew;
	
	public void setExcelTemplate(String excelTemplate) {
		this.excelTemplate = excelTemplate;
	}

	public void setPropertiesFolder(String propertiesFolder) {
		this.propertiesFolder = propertiesFolder;
	}

	public void setJsFile(String jsFile) {
		this.jsFile = jsFile;
	}

	public void setStartCreateNew(String startCreateNew) {
		this.startCreateNew = startCreateNew;
	}

	public String getExcelTemplate() {
		return excelTemplate;
	}

	public String getPropertiesFolder() {
		return propertiesFolder;
	}

	public String getJsFile() {
		return jsFile;
	}

	public String getStartCreateNew() {
		return startCreateNew;
	}

	private I18nExcel2Properties() {}
	
	public void excel2Properties() throws FileNotFoundException, Exception {
		
		int langCapacity = 4;
		
		final Map<String,Map<String,String>> i18Map = new HashMap<String,Map<String,String>>(langCapacity);
		
		final Map<Integer,String> m = new TreeMap<Integer,String>();
		
		final List<List<String>> jsList = new ArrayList<List<String>>();
		
		ExcelReader.readExcelContent(new FileInputStream(excelTemplate), ExcelVersion.V2007, new ExcelResultSet() {
			
			private int zh = 0;
			
			public boolean rowMapper(int index, String[] value) throws Exception {
				if(index == 0) {
					logger.debug("create i18n properties file..");
					int len = value.length;
					for(int i = 1 ; i < len; i++) {
						String langType = value[i];
						m.put(i, langType);
						Map<String,String> t = new HashMap<String,String>(len);
						i18Map.put(langType, t);
						
						if("zh".equals(langType)) {
							zh = i;
							
							m.put(len, "hk");
							Map<String,String> t1 = new HashMap<String,String>(len);
							i18Map.put("hk", t1);
						}
					}
				} else {
					int len = value.length;
					//check convert
					if(m.values().contains("zh")) {
						String[] vv = new String[len+1];
						System.arraycopy(value, 0, vv, 0, len);
						vv[len] = SimToTra(value[zh]);
						value = vv;
					}
					
					len = value.length;
					//prepared data
					String code = value[0];
					List<String> row = new ArrayList<String>(len);
					
					row.add(value[0]);
					for(int i = 1 ; i < len; i++) {
						String langType = m.get(i);

						Map<String,String> propertyMap = i18Map.get(langType);
						propertyMap.put(code, value[i]);
						
						row.add(value[i]);
						
					}
					jsList.add(row);
				}
				return true;
			}
		}, 0);
		
		//data to properties file
		Set<Entry<String,Map<String,String>>> set = i18Map.entrySet();
		Iterator<Entry<String,Map<String,String>>> itSet =  set.iterator();
		while(itSet.hasNext()) {
			Entry<String,Map<String,String>> a = itSet.next();
			String langType = a.getKey();
			Map<String,String> t = a.getValue();
			
			createProperties(langType,t);	
		}
		
		//data to js file
		//
		StringBuilder sb = new StringBuilder();
		sb.append("var I18nUtil=function(){var lang=$.cookie(\"lang\");var properties = {");

		for(List<String> list : jsList) {
			int len = list.size();
			sb.append("\"").append(list.get(0)).append("\"").append(":").append("[");
			for(int i = 1 ; i < len; i++) {
				sb.append("\"").append(list.get(i)).append("\",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("],");
		}
		sb.deleteCharAt(sb.length()-1);
				
		
		sb.append("};properties.getMessageByCode = function (key) {var idx = 0;");
		
		Collection<?> coll = m.values();
		int collLen = coll.size();
		
		Object[] appr = coll.toArray();
		
		for(int i = 0; i < collLen; i++) {
			if(i > 0) {
				sb.append("else ");
			}
			sb.append("if(lang==\"").append(appr[i]).append("\")idx=").append(i).append(";");
		}
		
		sb.append("return properties[key][idx];};return properties;}();");
		FileUtils.writeStringToFile(new File(jsFile), sb.toString());
		
	}
	
	private void createProperties(String langType,Map<String, String> map) throws IOException {
		File i18nFolder = new File(new StringBuilder().append(propertiesFolder).toString());
		if(!i18nFolder.exists())
			i18nFolder.mkdir();
		
		File file = new File(i18nFolder,new StringBuilder().append("messages_").append(langType).append(".properties").toString());
		if(file.exists())
			file.delete();
		file.createNewFile();
		
		Properties properties=new Properties();
		Set<Entry<String, String>> valueSet = map.entrySet();
		Iterator<Entry<String, String>> value= valueSet.iterator();
		while(value.hasNext()) {
			Entry<String, String> e = value.next();
			properties.setProperty(e.getKey(), e.getValue());
		}
		properties.store(new FileOutputStream(file),"i18n properties create by I18nExcel2Properties");
	}
	
    private String SimToTra(String simpStr) {  
        ZHConverter converter = ZHConverter  
                .getInstance(ZHConverter.TRADITIONAL);  
        String traditionalStr = converter.convert(simpStr);  
        return traditionalStr;  
    } 

}
