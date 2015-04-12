package com.rick.base.plugin.quartz.schedule;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ContextLoader;

import com.rick.base.plugin.quartz.model.ScheduleJob;

public final class ScheduleUtils {
	private ScheduleUtils() {}
	
	public static void invokMethod(ScheduleJob scheduleJob) throws Exception {
		Object obj = null;
		if(StringUtils.isNotBlank(scheduleJob.getBeanId())) {
			obj = ContextLoader.getCurrentWebApplicationContext().getBean(scheduleJob.getBeanId());
		} else {
			obj = Class.forName(scheduleJob.getBeanClass()).newInstance();
		}
		
		String name = scheduleJob.getMethodName();
		Class<?> c = obj.getClass();
		c.getMethod(name).invoke(obj);
		 
	}
}
