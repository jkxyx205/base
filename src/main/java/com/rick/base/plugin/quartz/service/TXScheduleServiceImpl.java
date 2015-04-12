package com.rick.base.plugin.quartz.service;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.plugin.quartz.model.ScheduleJob;
import com.rick.base.plugin.quartz.schedule.ScheduleUtils;

@Service("txService")
public class TXScheduleServiceImpl extends AbstractScheduleServiceImpl implements ScheduleService {
	private static final String TRIGGER_STATE_PAUSED = "PAUSED";
	
	private static final String TRIGGER_STATE_WAITING = "WAITING";
	
	@Resource	
	private BaseDaoImpl dao;
	
	@Resource
	private SchedulerFactoryBean schedulerFactoryBean;
	
 
	/**
	 * 立即执行job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void runJobNow(int id) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		if(scheduleJob == null) 
			return;
		
		if(ScheduleJob.STATUS_NOT_RUNNING.equals(scheduleJob.getJobStatus())) {
			try {
				ScheduleUtils.invokMethod(scheduleJob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SchedulerException(e);
			}
		} else {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.triggerJob(jobKey);
		}
	}
	
	public void pauseJob(int id) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		pauseJob(scheduleJob);
	}
	
	public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
		if(scheduleJob == null) 
			return;
		
		changJobStatusTx(TRIGGER_STATE_PAUSED,scheduleJob);
		changJobStatus(scheduleJob.getId(),ScheduleJob.STATUS_NOT_RUNNING);
		
	}
	
	private void changJobStatusTx(String status,ScheduleJob scheduleJob) {
		String sql = dao.getNamedQueryString("qrtz_changJobStatus");
		dao.getJdbcTemplate().update(sql, status,scheduleJob.getJobName(),scheduleJob.getJobGroup());
	}
	

	public void resumeJob(int id) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		if(scheduleJob == null) 
			return;
		
		changJobStatusTx(TRIGGER_STATE_WAITING,scheduleJob);
		changJobStatus(id,ScheduleJob.STATUS_RUNNING);
	}

	public void deleteJob(int id) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		if(scheduleJob == null) 
			return;
		//TODO
	}

	public void updateJobCron(int id,String cronExpression) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		if(scheduleJob == null || StringUtils.isBlank(cronExpression) || cronExpression.equals(scheduleJob.getCronExpression())) 
			return;
		
		
		super.updateJobCron(id, cronExpression);
		
		changeCronExpressTx(scheduleJob,cronExpression);
	}

	private void changeCronExpressTx(ScheduleJob scheduleJob,
			String cronExpression) {
		 String sql = dao.getNamedQueryString("qrtz_changCronExpression");
		 dao.getJdbcTemplate().update(sql, cronExpression,scheduleJob.getJobGroup(),scheduleJob.getJobName());
		
	}
}