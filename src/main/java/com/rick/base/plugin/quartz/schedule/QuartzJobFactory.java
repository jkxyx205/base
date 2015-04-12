package com.rick.base.plugin.quartz.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rick.base.plugin.quartz.model.ScheduleJob;
import com.rick.base.test.quartz.TestJobservice;

public class QuartzJobFactory implements Job{
	private static final transient Logger logger = LoggerFactory.getLogger(TestJobservice.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		
		try {
			ScheduleUtils.invokMethod(scheduleJob);
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
	}	
}
