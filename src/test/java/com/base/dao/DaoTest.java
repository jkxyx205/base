package com.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.dao.BaseDaoImpl.JdbcTemplateExecutor;
import com.rick.base.test.bean.SmScenario;
import com.rick.base.test.bean.SmScenarioVO;
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
	
	@Test
	public void testHibernateQuery() {
		HibernateTemplate tempate = dao.getHibernateTemplate();
		List<?> list = tempate.find("from com.rick.base.test.bean.SmScenario t where t.id = ?","1419328510900");
		System.out.println("list---------------------" + list);
		
		SmScenario s = new SmScenario();
		s.setId("1419328510900");
		List<?> list2 = tempate.findByExample(s);
		System.out.println("list2---------------------" + list2);
		
		List<?> list3 = tempate.findByNamedParam("from com.rick.base.test.bean.SmScenario t where t.id = :id", "id", "1419328510900");
		System.out.println("list3---------------------" + list3);
		
		List<?> list4 = tempate.findByNamedQuery("querySmscenario", "1419328510900");
		System.out.println("list4---------------------" + list4);
		
		List<?> list5 = tempate.findByValueBean("from com.rick.base.test.bean.SmScenario t where t.id = :id", s);
		System.out.println("list5---------------------" + list5);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SmScenario.class);
		List<?> list6 = tempate.findByCriteria(criteria);
		System.out.println("list6---------------------" + list6);
	}
	
	@Test
	public void testJDBCTemplateQuery() {
		//jdbcTemplate尽量只执行查询操作，莫要进行更新，否则会破坏Hibernate的二级缓存体系  
		String queryString = dao.getNamedQueryString("querySmscenario3");
		List<SmScenarioVO> list6 = dao.getJdbcTemplate().query(queryString, new Object[] {"1419328510900"}, new BeanPropertyRowMapper<SmScenarioVO>(SmScenarioVO.class));
		System.out.println("list6---------------------" + list6);
		
		
		Map<String,Object> param = new HashMap<String,Object>();
		//param.put("ID", "1419328510900");
		param.put("STORE_CODE", "3333;3204");
		
		List<SmScenarioVO> list7 = dao.queryForSpecificParam("querySmscenario4", param, new JdbcTemplateExecutor<List<SmScenarioVO>>() {
			public List<SmScenarioVO> query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
				return jdbcTemplate.query(queryString, args, new BeanPropertyRowMapper<SmScenarioVO>(SmScenarioVO.class));
			}
		});
		
		System.out.println("list7---------------------" +list7);
		
		List<Map<String,Object>> list8 = dao.queryForSpecificParam("querySmscenario4", param, new JdbcTemplateExecutor<List<Map<String,Object>>>() {
			public List<Map<String,Object>> query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
				return jdbcTemplate.queryForList(queryString, args);
			}
		});
		
		System.out.println("list8---------------------" +list8);
		
		List<SmScenarioVO> list9 = dao.queryForSpecificParam("querySmscenario4", param, new JdbcTemplateExecutor<List<SmScenarioVO>>() {
			public  List<SmScenarioVO> query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
				return jdbcTemplate.query(queryString, args,new RowMapper<SmScenarioVO>() {

					public SmScenarioVO mapRow(ResultSet rs, int arg1)
							throws SQLException {
						SmScenarioVO vo = new SmScenarioVO();
						vo.setId(rs.getString(1));
						return vo;
					}
					
				});
			}
		});
		
		System.out.println("list9---------------------" +list9);
	}
	
	
	@Test
	public void testQueryCount2() {
		//Map<String,Object> param = new HashMap<String,Object>();
		//param.put("ID", "1419328510900");
		//param.put("STORE_CODE", "22222;");
		//long count = dao.queryForSpecificParamCount("querySmscenario4", param);
		//System.out.println(count);
	}
	

}
