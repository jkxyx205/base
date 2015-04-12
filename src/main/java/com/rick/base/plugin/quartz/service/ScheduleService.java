package com.rick.base.plugin.quartz.service;

import org.quartz.SchedulerException;

public interface ScheduleService {
	
	public void init() throws SchedulerException;
	
	/**
	 * 立即执行job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void runJobNow(int id) throws SchedulerException;
	
	/**
	 * 暂停一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void pauseJob(int id) throws SchedulerException;
	
	/**
	 * 恢复一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void resumeJob(int id) throws SchedulerException;
	
	/**
	 * 删除一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void deleteJob(int id) throws SchedulerException;
	
	/**
	 * 更新job时间表达式
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void updateJobCron(int id, String cronExpression) throws SchedulerException;
}
