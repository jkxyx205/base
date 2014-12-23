package com.rick.base.test.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJobservice {
	private static final transient Logger logger = LoggerFactory.getLogger(TestJobservice.class);  
	
	public void executeTask() {
		logger.info("execute task! ..........");
	}
}
