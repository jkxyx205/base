package com.rick.base.plugin.quartz.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.quartz.SchedulerException;
import org.quartz.simpl.RAMJobStore;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service("quartzManager")
public class QuartzManager implements ApplicationContextAware{

	@Resource
	private SchedulerFactoryBean schedulerFactoryBean;
	
	private ScheduleService scheduleService;
	
	@PostConstruct
	public void init() throws SchedulerException {
		
		Class<?> clazz = schedulerFactoryBean.getScheduler().getMetaData().getJobStoreClass();
		if(RAMJobStore.class == clazz) {
			scheduleService = (ScheduleService) applicationContext.getBean("ramService");
		} else {
			scheduleService = (ScheduleService) applicationContext.getBean("txService");
		}
		
		scheduleService.init();
	}

	public void runJobNow(int id) throws SchedulerException {
		scheduleService.runJobNow(id);
		
	}

	public void pauseJob(int id) throws SchedulerException {
		scheduleService.pauseJob(id);
		
	}

	public void resumeJob(int id) throws SchedulerException {
		scheduleService.resumeJob(id);
		
	}

	public void deleteJob(int id) throws SchedulerException {
		scheduleService.deleteJob(id);
		
	}

	public void updateJobCron(int id, String cronExpression)
			throws SchedulerException {
		scheduleService.updateJobCron(id, cronExpression);
		
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	private ApplicationContext applicationContext;

}
