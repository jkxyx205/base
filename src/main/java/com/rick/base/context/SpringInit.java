package com.rick.base.context;

import java.io.File;
import java.io.FileNotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.rick.base.context.Constants.ConstantsBuilder;
import com.rick.base.dictionary.service.DicService;

public class SpringInit implements ServletContextListener {
    
	private static final transient Logger logger = LoggerFactory.getLogger(SpringInit.class);
	
    private static WebApplicationContext springContext;
    
    public static WebApplicationContext getSpringContext() {
		return springContext;
	}

	public SpringInit() {
        super();
    }
    
    public void contextInitialized(ServletContextEvent event) {
        springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        initConstants();
        initI18n();
        initDictionary();
    }
    
    private void initDictionary() {
    	DicService dicService = springContext.getBean("dicService", DicService.class);
    	try {
			dicService.initEnv();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initConstants() {
    	ServletContext sc = springContext.getServletContext();
    	
    	FileConstants fc = springContext.getBean("fileConstants",FileConstants.class);
    	
    	try {
			String contextDir = WebUtils.getRealPath(sc, File.separator);
			File tmp = WebUtils.getTempDir(sc);
			Constants.instance = new ConstantsBuilder().contextDir(contextDir)
					.tmpDir(tmp)
					.i18nPropertiesFolder(contextDir + fc.getPropertiesFolder())
					.i18nJsFile(contextDir + fc.getJsFile())
					.i18nExcelTemplate(contextDir + fc.getExcelTemplate())
					.dictionaryProFolder(contextDir + fc.getDicProFolder())
					.dictionaryFile(contextDir + fc.getDicJsFile())
					.build();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}		
	}

	private void initI18n() {
    	I18nProperties pro = springContext.getBean("i18nExcel2Properties", I18nProperties.class);
	        if("true".equals(pro.getStartCreateNew())) {
	        	try {
					pro.excel2Properties();
				}  catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
	        }
    }

    public void contextDestroyed(ServletContextEvent event) {
    	
    }
    
}