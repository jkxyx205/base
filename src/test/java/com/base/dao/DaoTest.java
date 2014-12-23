package com.base.dao;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.test.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "classpath:system/applicationContext-*.xml","classpath:business/applicationContext-*.xml" }) 
public class DaoTest extends AbstractJUnit4SpringContextTests {
	
	@Resource(name = "baseDao")
	private BaseDaoImpl dao;
	
	@Value("${jdbc.driverClass}")
	private String name;
	
	@Resource
	private TestService service;
	
	@Test
	public void testBaseDao() {
		System.out.println(dao.getConnection());
		System.out.println(dao.getJdbcTemplate());
		System.out.println(dao.getSessionFactory());
	}
	
	/**
	 * Test usage of property-placeholder
	 */
	@Test
	public void testUtils() {
		System.out.println(name);
		MessageSource resources = this.applicationContext;
		System.out.println(resources.getMessage("rick", null, null));
		//System.out.println(this.applicationContext.getMessage("rick", null, new Locale("")));
	}
	
	@Test
	public void testTrans() throws Exception {
		service.add1();
		System.out.println(service);
	}
	
	@Test
	public void testSession() {
		//不在事务中，抛出异常  org.hibernate.HibernateException: No Session found for current thread
	    Session session  = dao.getSessionFactory().getCurrentSession(); 
	    System.out.println(session);
	}
	

}
