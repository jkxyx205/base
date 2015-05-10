package com.base.dao;	

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.dao.TableGenerator;
import com.rick.base.test.bean.Emp;
import com.rick.base.test.bean.Permission;
import com.rick.base.test.bean.User;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"classpath:system/applicationContext-util.xml", 
									"classpath:system/applicationContext-hibernate.xml",
									"classpath:system/applicationContext-mapping.xml"
									}) 
public class MappingTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	protected ApplicationContext ctx;
	
	
	@Resource(name = "baseDao")
	private BaseDaoImpl dao;
	
	@Resource(name = "tableGenerator")
	private TableGenerator tableGenerator;
	
	@Test
	public void testGet() {
		//Emp e = dao.get(Emp.class, 1111);
		//Emp e = dao.get(Emp.class, 7369);
		//System.out.println(e);
		
	/*	@SuppressWarnings("serial")
		List<Emp> list =  dao.query(Emp.class, "empno in (:empno) and ename like :ename", new HashMap<String,Object>() {
			{
				put("empno","1111;2222");
				put("ename","Ashley");
			}
		});*/
		
		List<Emp> list = dao.query(Emp.class, "", null);
		
		for (Emp ee : list) {
			System.out.println(ee.getEname() + ".........." + ee.getHdate());
		}
		
	}
	
	@Test
	public void testAdd() {
	/*	Emp e = dao.get(Emp.class, 7369);
		e.setEmpno(1234);
		dao.save(e);*/
		
		Emp e = new Emp();
		e.setEname("Ashley");
		e.setHdate(new Date());
		e.setSal(3699.2f);
		dao.save(e);
		
/*		System.out.println(ctx);
		System.out.println(ctx.getEnvironment().getProperty("jdbc.url"));*/
		 
		
	}
	
	@Test
	public void testDelete() {
		Emp e = dao.get(Emp.class, 1234);
		dao.delete(e);
		
		//dao.getHibernateTemplate().saveOrUpdate(entity);
		//dao.getHibernateTemplate().deleteAll(entities);
		//dao.getHibernateTemplate().update(entity);
	}
	
	@Test
	public void testUpdate() {
		Emp e = dao.get(Emp.class, 1111);
		e.setEmpno(1234);
		System.out.println(e);
		e.setEname("finethank");
		/*e.setEname("rickashley");
		dao.update(e);*/
		
		//dao.getHibernateTemplate().saveOrUpdate(entity);
		//dao.getHibernateTemplate().deleteAll(entities);
		//dao.getHibernateTemplate().update(entity);
		//dao.getHibernateTemplate().get(entityClass, id)
		dao.saveOrUpdate(e);
	}
	
	@Test
	public void create() {
		tableGenerator.createTable(User.class);
	}
	
	@Test
	public void testJdbcTemplate() {
	   Permission per = new Permission();
	   per.setDescription("rick");
	   per.setPermission("five11");
	   per.setAvailable(true);
	   dao.save(per);
	   
	   System.out.println("................" + per.getId());
	}
	
	
}
 
