package com.rick.base.context;

import java.io.FileNotFoundException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.rick.base.util.I18nExcel2Properties;

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
        
        initI18n();
        
        
    }
    
    private void initI18n() {
    	I18nExcel2Properties pro = springContext.getBean("I18nExcel2Properties", I18nExcel2Properties.class);
        
	       try {
	        	pro.setJsFile(WebUtils.getRealPath(springContext.getServletContext(), pro.getJsFile()));
	        	pro.setExcelTemplate(WebUtils.getRealPath(springContext.getServletContext(), pro.getExcelTemplate()));
	        	pro.setPropertiesFolder(WebUtils.getRealPath(springContext.getServletContext(), pro.getPropertiesFolder()));
			 
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
			}
	        
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