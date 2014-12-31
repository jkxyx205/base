package com.rick.base.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class FileConstants {
	@Value("${excelTemplate}")
	private String  excelTemplate;
	
	@Value("${propertiesFolder}")
	private String  propertiesFolder;
	
	@Value("${jsFile}")
	private String  jsFile;
	
	@Value("${dicJsPath}")	
	private String dicJsFile;
	
	@Value("${xmlPath}")
	private String dicProFolder;
	
	public String getDicJsFile() {
		return dicJsFile;
	}

	public String getDicProFolder() {
		return dicProFolder;
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
}
