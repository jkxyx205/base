package com.rick.base.plugin.quartz.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.plugin.quartz.model.ScheduleJob;
import com.rick.base.plugin.quartz.schedule.QuartzJobFactory;
import com.rick.base.plugin.quartz.schedule.ScheduleUtils;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	private static final transient Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
	
	@Resource	
	private BaseDaoImpl dao;
	
	@Resource
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@PostConstruct
	public void init() throws SchedulerException {
		List<ScheduleJob> list = getAllJobs();
		for(ScheduleJob scheduleJob : list) {
			addJob(scheduleJob);
		}
		
	}
	
	private void addJob(ScheduleJob scheduleJob) throws SchedulerException {
		if (scheduleJob == null) {
			return;
		}
		
		if(logger.isDebugEnabled())
			logger.debug("add job " + scheduleJob.getJobGroup() + "_" +scheduleJob.getJobName());
		
		
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		// 不存在，创建一个
		if (null == trigger) {
			//Class clazz = ScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
			JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).build();

			jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

			trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
		
		if(ScheduleJob.STATUS_NOT_RUNNING.equals(scheduleJob.getJobStatus())) {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.pauseJob(jobKey);
		}
		 
	}
	
	
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
	
	
	public void updateJobCron(int id,String cronExpression) throws SchedulerException {
		ScheduleJob scheduleJob = getScheduleJobById(id);
		if(scheduleJob == null || StringUtils.isBlank(cronExpression) || cronExpression.equals(scheduleJob.getCronExpression())) 
			return;
		
		
		scheduleJob.setCronExpression(cronExpression);
		
		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		
		scheduler.rescheduleJob(triggerKey, trigger);
 
		if(ScheduleJob.STATUS_NOT_RUNNING.equals(scheduleJob.getJobStatus()))  {
				JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
				scheduler.pauseJob(jobKey);
		}
			
		changeCronExpress(id, cronExpression);
		
	}
	
	 
	
	public void changeCronExpress(int id,String cronExpress) {
		String sql = dao.getNamedQueryString("changJobCronExpress");
		dao.getJdbcTemplate().update(sql, cronExpress,id);
	}
	
	public ScheduleJob getScheduleJobById(int id) {
		String sql = dao.getNamedQueryString("getScheduleJobById");
		
		return dao.getJdbcTemplate().query(sql, new Object[] {id}, new ResultSetExtractor<ScheduleJob>() {

			public ScheduleJob extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				ScheduleJob job = null;
			    if(rs.next()) {
			    	job = new ScheduleJob();
					job.setJobName(rs.getString(1));
					job.setJobGroup(rs.getString(2));
					job.setCronExpression(rs.getString(3));
					job.setMethodName(rs.getString(4));
					job.setBeanClass(rs.getString(5));
					job.setBeanId(rs.getString(6));
					job.setJobStatus(rs.getString(7));
					job.setId(rs.getInt(8));
			    }
				return job;
			}
			 
		});
	}
	
	public List<ScheduleJob> getAllJobs() {
		List<ScheduleJob> list = dao.getJdbcTemplate().query(ALL_JOB_SQL, new RowMapper<ScheduleJob>() {

			public ScheduleJob mapRow(ResultSet rs, int idx)
					throws SQLException {
				ScheduleJob job = new ScheduleJob();
				job.setJobName(rs.getString(1));
				job.setJobGroup(rs.getString(2));
				job.setCronExpression(rs.getString(3));
				job.setMethodName(rs.getString(4));
				job.setBeanClass(rs.getString(5));
				job.setBeanId(rs.getString(6));
				job.setJobStatus(rs.getString(7));
				job.setId(rs.getInt(8));
				return job;
			}
			
		});
		
		return list;
	}
	
	public void changJobStatus(int id,String status) {
		String sql = dao.getNamedQueryString("changJobStatus");
		dao.getJdbcTemplate().update(sql, status,id);
	}
	
	private static final String ALL_JOB_SQL = "select t.job_name,t.job_group,t.cron_expression,t.method_name,t.bean_class, t.bean_id,t.job_status,t.id from QUARTZ_SCHEDULE_JOB t" ;
	
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