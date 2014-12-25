package com.rick.base.test.quartz;

import java.lang.reflect.Method;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.rick.base.test.TestController;

public class DetailQuartzJobBean extends QuartzJobBean {
	private static final transient Logger logger = LoggerFactory.getLogger(TestController.class); 
	
	private ApplicationContext applicationContext;  
	 
	private String targetObject;
	 
	private String targetMethod;
	 
     public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	  
     /** 
	   * 从SchedulerFactoryBean注入的applicationContext. 
	   */  
     public void setApplicationContext(ApplicationContext applicationContext) {  
        this.applicationContext = applicationContext;  
    }  
	  
	    @Override  
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		try {

			logger.info("execute [" + targetObject + "] at once>>>>>>");
			
			Object otargetObject = applicationContext.getBean(targetObject);
			Method m = null;
			try {
				m = otargetObject.getClass().getMethod(targetMethod,
						new Class[] {});

				m.invoke(otargetObject, new Object[] {});
			} catch (SecurityException e) {
				 logger.error(e.getMessage());
			} catch (NoSuchMethodException e) {
				 logger.error(e.getMessage());
			}

		} catch (Exception e) {
			throw new JobExecutionException(e);
		}

	}
}