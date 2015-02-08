package com.rick.base.dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.rick.base.dao.sqlreader.SQLReader;

/**
 * @author Rick.Xu
 * 
 */
@Repository("baseDao")
public class BaseDaoImpl {
	private static final transient Logger logger = LoggerFactory
			.getLogger(BaseDaoImpl.class);

	private static final String PARAM_IN_SEPERATOR = ";";
	
	private Dialect dialect = Dialect.ORACLE;
	
	private AbstractSqlFormatter sqlFormatter;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	@Resource(name = "hibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
	@PostConstruct
	public void init() {
		if(dialect == Dialect.ORACLE)
			sqlFormatter = new OracleSqlFormatter();
		else if(dialect == Dialect.MYSQL)
			sqlFormatter = new MysqlSqlFormatter();
	}
	
	public Dialect getDialect() {
		return dialect;
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Connection getConnection() {
		return DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
	}

	public void closeConnection(Connection conn) {
		DataSourceUtils.releaseConnection(conn, jdbcTemplate.getDataSource());
	}

	public Session getSession() {
		SessionFactory sessionFactory = getHibernateTemplate()
				.getSessionFactory();
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		if (session == null) {
			session = sessionFactory.openSession();
			session.setFlushMode(FlushMode.MANUAL);
		}
		return session;
	}

	public void closeSession(Session session) {
		SessionFactoryUtils.closeSession(session);
	}

	public String getNamedQueryString(String queryName) {
		/*SessionFactoryImpl factory = (SessionFactoryImpl) getSessionFactory();
		NamedSQLQueryDefinition nqd = factory.getNamedSQLQuery(queryName);
		return nqd.getQueryString();*/
		try {
			return SQLReader.getSQLbyName(queryName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public <T> T queryForSpecificParam(String queryName,
			Map<String, Object> param, String paramInSeperator,
			JdbcTemplateExecutor<T> jdbcTemplateExecutor) {
		String sql = getNamedQueryString(queryName);
		Map<String, Object> formatMap = new HashMap<String, Object>();
		String formatSql = sqlFormatter.formatSql(sql, param, formatMap,
				paramInSeperator);

		Object[] args = NamedParameterUtils.buildValueArray(formatSql,
				formatMap);
		logger.debug(formatSql + "=>" + formatMap);
		return jdbcTemplateExecutor.query(jdbcTemplate, formatSql, args);
	}
	
	public void executeForSpecificParam(String queryName,
			Map<String, Object> param, String paramInSeperator) {
		String sql = getNamedQueryString(queryName);
		Map<String, Object> formatMap = new HashMap<String, Object>();
		String formatSql = sqlFormatter.formatSql(sql, param, formatMap,
				paramInSeperator);

		Object[] args = NamedParameterUtils.buildValueArray(formatSql,
				formatMap);
		
		jdbcTemplate.update(formatSql, args);
		logger.debug(formatSql + "=>" + formatMap);
	}
	
	public void executeForSpecificParam(String queryName,
			Map<String, Object> param) {
		executeForSpecificParam(queryName,
				param,PARAM_IN_SEPERATOR);
	}
	

	public <T> T queryForSpecificParam(String queryName,
			Map<String, Object> param,
			JdbcTemplateExecutor<T> jdbcTemplateExecutor) {
		return queryForSpecificParam(queryName, param, PARAM_IN_SEPERATOR,
				jdbcTemplateExecutor);
	}

	public long queryForSpecificParamCount(String queryName,
			Map<String, Object> param, String paramInSeperator,JdbcTemplateExecutor<Long> jdbcTemplateExecutor) {
		String sql = getNamedQueryString(queryName);
		Map<String, Object> formatMap = new HashMap<String, Object>();
		String formatSql = sqlFormatter.formatSql(sql, param, formatMap,
				paramInSeperator);
		formatSql = sqlFormatter.formatSqlCount(formatSql);
		Object[] args = NamedParameterUtils.buildValueArray(formatSql,
				formatMap);
		logger.debug(formatSql + "=>" + formatMap);
		return jdbcTemplate.queryForObject(formatSql, args, Long.class);
	}

	public String formatSqlCount(String srcSql) {
		return sqlFormatter.formatSqlCount(srcSql);
	}
	
	public String formatSql(String srcSql,Map<String,Object> param,Map<String, Object> formatMap,String paramInSeperator) {
		return sqlFormatter.formatSql(srcSql, param, formatMap, paramInSeperator);
	}
	
	
	
	public long queryForSpecificParamCount(String queryName,
			Map<String, Object> param,JdbcTemplateExecutor<Long> jdbcTemplateExecutor) {
		return queryForSpecificParamCount(queryName, param, PARAM_IN_SEPERATOR,jdbcTemplateExecutor);
	}

	public interface JdbcTemplateExecutor<T> {
		public T query(JdbcTemplate jdbcTemplate, String queryString,
				Object[] args);
	}
 
	

}
