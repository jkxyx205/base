package com.rick.base.context;

import java.io.File;

import com.rick.base.office.excel.Builder;

public final class Constants {
	public static Constants getInstance() {
		return instance;
	}

	public final File TMP_DIR;
	
	public final String CONTEXT_DIR;
	
	public final String I18N_PROPERTIES_FOLDER;
	
	public final String I18N_EXCEL_TEMPLATE;
	
	public final String I18N_JS_FILE;
	
	public final String DICTIONARY_PROPERTIES_FOLDER;
	
	public final String DICTIONARY_JS_FILE;
	
	public static final String DATE_FORMAT = "yyyy/MM/dd";
	
	
	static Constants instance;

	static class ConstantsBuilder implements Builder<Constants> {
		private File tmpDir;
		
		private String contextDir;
		
		private String propertiesFolder;
		
		private String  excelTemplate;
		
		private String  jsFile;
		
		private String dictionaryProFolder;
		
		private String dictionaryFile;
		
		public ConstantsBuilder tmpDir(File tmpDir) {
			this.tmpDir = tmpDir;
			return this;
		}
		
		public ConstantsBuilder contextDir(String contextDir) {
			this.contextDir = contextDir;
			return this;
		}
		
		public ConstantsBuilder i18nPropertiesFolder(String propertiesFolder) {
			this.propertiesFolder = propertiesFolder;
			return this;
		}
		
		public ConstantsBuilder i18nExcelTemplate(String excelTemplate) {
			this.excelTemplate = excelTemplate;
			return this;
		}
		public ConstantsBuilder i18nJsFile(String jsFile) {
			this.jsFile = jsFile;
			return this;
		}
		
		public ConstantsBuilder dictionaryProFolder(String dictionaryProFolder) {
			this.dictionaryProFolder = dictionaryProFolder;
			return this;
		}
		public ConstantsBuilder dictionaryFile(String dictionaryFile) {
			this.dictionaryFile = dictionaryFile;
			return this;
		}
		
		
		
		public Constants build() {
			return new Constants(this);
		}
		
	} 
	
	private Constants(ConstantsBuilder builder) {
		this.TMP_DIR = builder.tmpDir;
		this.CONTEXT_DIR = builder.contextDir;
		this.I18N_PROPERTIES_FOLDER = builder.propertiesFolder;
		this.I18N_EXCEL_TEMPLATE = builder.excelTemplate;
		this.I18N_JS_FILE = builder.jsFile;
		
		this.DICTIONARY_JS_FILE = builder.dictionaryFile;
		this.DICTIONARY_PROPERTIES_FOLDER = builder.dictionaryProFolder;
	}
}
