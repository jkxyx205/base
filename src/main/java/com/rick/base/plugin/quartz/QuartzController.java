package com.rick.base.plugin.quartz;

import javax.annotation.Resource;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rick.base.plugin.quartz.service.QuartzManager;

@Controller
@RequestMapping("/quartz")
public class QuartzController {
	
	@Resource
	private QuartzManager manager;
	
	@RequestMapping("/runJobNow/{id}")
	@ResponseBody
	public String execute(@PathVariable int id) {
		String res = "fail";
		try {
			manager.runJobNow(id);
			res = "success";
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@RequestMapping("/resumeJob/{id}")
	@ResponseBody
	public String resumeJob(@PathVariable int id) {
		String res = "fail";
		try {
			manager.resumeJob(id);
			res = "success";
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	@RequestMapping("/pauseJob/{id}")
	@ResponseBody
	public String pauseJob(@PathVariable int id) {
		String res = "fail";
		try {
			manager.pauseJob(id);
			res = "success";
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	@RequestMapping("/confJob")
	@ResponseBody
	public String confJob(int id,String cronExpression) {
		String res = "fail";
		try {
			manager.updateJobCron(id, cronExpression);
			res = "success";
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
}
