package com.rick.base.test.service;
        
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Service;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.test.bean.SmScenario;

@Service("tservice")
//@Transactional
public class TestService/* implements ITestService */{
	@Resource(name = "baseDao")
	private BaseDaoImpl dao;

	public void add1() throws Exception {
		Session session = dao.getSession();
		System.out.println("add1 :-----------------------------------" + session);
		session.createSQLQuery("INSERT INTO SM_SCENARIO(ID,STORE_CODE) VALUES(SYS_GUID(),'11111')").executeUpdate();
		session.flush();
		// 事务环境中session不需要关闭。 session.close();
		add2();
	}

	public void add2() throws Exception {
		Session session = dao.getSession();
//		System.out.println("add2 :-----------------------------------" + session);
		SmScenario s = new SmScenario();
		s.setId(String.valueOf(System.currentTimeMillis()));
		s.setStoreCode("22222");
		session.save(s);
		session.flush();
		// 事务环境中session不需要关闭。 session.close();
		add3();
	}
	
	public void add3() throws SQLException {
		Connection conn = dao.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO SM_SCENARIO(ID,STORE_CODE) VALUES(SYS_GUID(),'33333')");
		pstmt.executeUpdate();
		pstmt.close();
		dao.closeConnection(conn);
		add4();
	}
	
	public void add4() {
		dao.getHibernateTemplate().execute(new HibernateCallback<SmScenario>() {
			public SmScenario doInHibernate(Session session) throws HibernateException {
				SmScenario s = new SmScenario();
				s.setId(String.valueOf(System.currentTimeMillis()));
				s.setStoreCode("44444");
				session.save(s);
				session.flush();
				return s;
			}
		});
		add5();
	}
	
	public void add5() {
		dao.getJdbcTemplate().execute("INSERT INTO SM_SCENARIO(ID,STORE_CODE) VALUES(SYS_GUID(),'55555')");
		//抛出RunntimeException异常
		//int a = 1/0;
		//全部rollback
	} 
}
