package com.rick.base.plugin.quartz.service;

import javax.annotation.Resource;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.plugin.quartz.model.ScheduleJob;

@Service("ramService")
public class RAMScheduleServiceImpl extends AbstractScheduleServiceImpl implements ScheduleService {
	//private static final transient Logger logger = LoggerFactory.getLogger(RAMScheduleServiceImpl.class);
	
	@Resource	
	private BaseDaoImpl dao;
	
	@Resource
	private SchedulerFactoryBean schedulerFactoryBean;
	

	
	public void pauseJob(int id) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		pauseJob(scheduleJob);
	}
	
	public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
		if(scheduleJob == null) 
			return;
		
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.pauseJob(jobKey);
		
		changJobStatus(scheduleJob.getId(),ScheduleJob.STATUS_NOT_RUNNING);
		
	}
	

	public void resumeJob(int id) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		if(scheduleJob == null) 
			return;
		
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.resumeJob(jobKey);
		
		changJobStatus(id,ScheduleJob.STATUS_RUNNING);
	}

	public void deleteJob(int id) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		if(scheduleJob == null) 
			return;
		
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.deleteJob(jobKey);
	}

}