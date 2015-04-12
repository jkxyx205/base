package com.rick.base.test.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rick.base.dao.BaseDaoImpl;


@Service("testJobservice")
public class TestJobservice {
	private static final transient Logger logger = LoggerFactory.getLogger(TestJobservice.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Resource
	private BaseDaoImpl dao;
	
	public void executeTask() {
		logger.info("execute task11111111111111! .........."+ dao + "  =>"+ sdf.format(new Date()));
	}
}
