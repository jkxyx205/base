package com.rick.base.test.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestJobservice2 {
	private static final transient Logger logger = LoggerFactory.getLogger(TestJobservice2.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public void executeTask() {
		logger.info("execute task22222222222222! ..........  =>"+ sdf.format(new Date()));
	}
}
