package com.rick.base.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.GenerationType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

import com.rick.base.dao.boot.EntityDesc;
import com.rick.base.dao.boot.EntityReader;
import com.rick.base.dao.boot.SQLReader;

/**
 * @author Rick.Xu
 * 
 */
public class BaseDaoImpl {
	private static final transient Logger logger = LoggerFactory
			.getLogger(BaseDaoImpl.class);

	private static final String PARAM_IN_SEPERATOR = ";";
	
	private Dialect dialect = Dialect.ORACLE;
	
	private AbstractSqlFormatter sqlFormatter;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	//@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	//@Resource(name = "hibernateTemplate")
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
	/*	SessionFactoryImpl factory = (SessionFactoryImpl) getSessionFactory();
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
	
	public  <T> T queryForSpecificParam(String queryName,
			Object param,
			JdbcTemplateExecutor<T> jdbcTemplateExecutor) {
		Map<String, Object> map = Utils.bean2Map(param);
		return queryForSpecificParam(queryName, map, PARAM_IN_SEPERATOR,
				jdbcTemplateExecutor);
	}
	
	public <T> T queryForSpecificParam(String queryName,
			Object param,String paramInSeperator,
			JdbcTemplateExecutor<T> jdbcTemplateExecutor) {
		Map<String, Object> map = Utils.bean2Map(param);
		return queryForSpecificParam(queryName, map, PARAM_IN_SEPERATOR,
				jdbcTemplateExecutor);
	}
	
	
	public <T> T queryForSpecificParam(String queryName,
			Map<String, Object> param,
			JdbcTemplateExecutor<T> jdbcTemplateExecutor) {
		return queryForSpecificParam(queryName, param, PARAM_IN_SEPERATOR,
				jdbcTemplateExecutor);
	}
	
	public int executeForSpecificParam(String queryName,
			Map<String, Object> param, String paramInSeperator) {
		String sql = getNamedQueryString(queryName);
		Map<String, Object> formatMap = new HashMap<String, Object>();
		String formatSql = sqlFormatter.formatSql(sql, param, formatMap,
				paramInSeperator);

		Object[] args = NamedParameterUtils.buildValueArray(formatSql,
				formatMap);
		
		int count = jdbcTemplate.update(formatSql, args);
		logger.debug(formatSql + "=>" + formatMap);
		
		return count;
	}
	
	public int executeForSpecificParam(String queryName,
			Map<String, Object> param) {
		return executeForSpecificParam(queryName,
				param,PARAM_IN_SEPERATOR);
	}
	
	public int executeForSpecificParam(String queryName,
			Object param) {
		Map<String, Object> map = Utils.bean2Map(param);
		return executeForSpecificParam(queryName,
				map,PARAM_IN_SEPERATOR);
	}
	
	public int executeForSpecificParam(String queryName,
			Object param, String paramInSeperator) {
		Map<String, Object> map = Utils.bean2Map(param);
		return executeForSpecificParam(queryName,
				map,PARAM_IN_SEPERATOR);
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

	
	public long queryForSpecificParamCount(String queryName,
			Map<String, Object> param,JdbcTemplateExecutor<Long> jdbcTemplateExecutor) {
		return queryForSpecificParamCount(queryName, param, PARAM_IN_SEPERATOR,jdbcTemplateExecutor);
	}

	public long queryForSpecificParamCount(String queryName,
			Object param,JdbcTemplateExecutor<Long> jdbcTemplateExecutor) {
		Map<String, Object> map = Utils.bean2Map(param);
		return queryForSpecificParamCount(queryName, map, PARAM_IN_SEPERATOR,jdbcTemplateExecutor);
	}

	
	public long queryForSpecificParamCount(String queryName,
			Object param,String paramInSeperator,JdbcTemplateExecutor<Long> jdbcTemplateExecutor) {
		Map<String, Object> map = Utils.bean2Map(param);
		return queryForSpecificParamCount(queryName, map, PARAM_IN_SEPERATOR,jdbcTemplateExecutor);
	}
	
	public String formatSqlCount(String srcSql) {
		return sqlFormatter.formatSqlCount(srcSql);
	}
	
	public String formatSql(String srcSql,Map<String,Object> param,Map<String, Object> formatMap,String paramInSeperator) {
		return sqlFormatter.formatSql(srcSql, param, formatMap, paramInSeperator);
	}
	


	public interface JdbcTemplateExecutor<T> {
		public T query(JdbcTemplate jdbcTemplate, String queryString,
				Object[] args);
	}
	
	public <T> T get(final Class<T> clazz,Serializable s) {
		final EntityDesc ed = EntityReader.getEntityDesc(clazz);
		
		if(ed == null)
			return null;
		
		StringBuilder sb = new StringBuilder("SELECT ");
		
		for(EntityDesc.Column c : ed.getColumn()) {
			sb.append(c.getDbColumnName()).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" FROM ")
			.append(ed.getTableName())
			.append(" WHERE ")
			.append(ed.getPrimaryKey())
			.append(" = ")
			.append(":").append(ed.getPrimaryKey());
	 
		logger.debug(sb.toString());
		
		Map<String,Object> param = new HashMap<String,Object>(1);
		param.put(ed.getPrimaryKey(), s);
		
		Object[] args = NamedParameterUtils.buildValueArray(sb.toString(),
				param);
		
		return jdbcTemplate.query(sb.toString(), args, new ResultSetExtractor<T>() {

			public T extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if(rs.next()) {
					T t = null;
					try {
						t = clazz.newInstance();
						for(EntityDesc.Column c : ed.getColumn()) {
							Object value = rs.getObject(c.getDbColumnName());
							if(value == null)
								 continue;
							 
						  BeanUtils.setProperty(t, c.getClazzProName(), value);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e.getMessage());
						throw new RuntimeException(e);
					}
					return t;
				} else {
					return null;
				}
			}
		});
	}
	
	public Serializable save(Object entity) {
		Class<?> clazz = entity.getClass();
		final EntityDesc ed = EntityReader.getEntityDesc(clazz);
		
		if(ed == null) {
			throw new RuntimeException("No such entity "+ entity.getClass());
		}
		
		int paramLen = 0;
		int len = paramLen = ed.getColumn().size();
 
		Serializable id = null;
		boolean insertId = true;
		
		Object[] args = new Object[paramLen];
		
		
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(ed.getTableName());
		sb.append("(");
	
		try {
			if(GenerationType.IDENTITY == ed.getType()) {
				//select LAST_INSERT_ID() mysql 获取id
				paramLen = len - 1;
				insertId = false;
			} else if (GenerationType.SEQUENCE == ed.getType()){
				String seqSql = "SELECT "+ed.getTableName()+"_"+ed.getPrimaryKey()+".NEXTVAL FROM DUAL";
				id = jdbcTemplate.queryForObject(seqSql, Integer.class);
				PropertyUtils.setProperty(entity, ed.getPrimaryKey(), id);
			} else if (GenerationType.TABLE == ed.getType()){
				//id =
			}  else if (GenerationType.AUTO == ed.getType()){
				id = (Serializable) PropertyUtils.getProperty(entity, ed.getPrimaryKey());
				if (null == id) {// 没有指定主键  采用UUID,这个时候 id字段类型要采用String
					//TODO
					//PropertyUtils.setProperty(entity, ed.getPrimaryKey(), 990);
					logger.error("primary key can't be null");
					throw new RuntimeException("primary key can't be null");
				}
			}  
			
			int index = 0;
			for (EntityDesc.Column c: ed.getColumn()) {
				if(!insertId && c.getDbColumnName().equals(ed.getPrimaryKey())) 
					continue;
				
				sb.append(c.getDbColumnName()).append(",");
				args[index++] = PropertyUtils.getProperty(entity, c.getClazzProName());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") VALUES(");
		
		
		for(int i = 0; i < paramLen; i++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		
		logger.debug(sb.toString());
		jdbcTemplate.update(sb.toString(), args);
		return id;
	}
	
	public void saveOrUpdate(Object entity) {
		Class<?> clazz = entity.getClass();
		EntityDesc ed = EntityReader.getEntityDesc(clazz);
		
		try {
			Serializable id = (Serializable) PropertyUtils.getProperty(entity, ed.getPrimaryKey());
			Object obj = get(clazz, id);
			if(obj == null) {
				save(entity);
			} else {
				update(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	
	public void update(Object entity) {
		Class<?> clazz = entity.getClass();
		final EntityDesc ed = EntityReader.getEntityDesc(clazz);
		
		if(ed == null) {
			throw new RuntimeException("No such entity "+ entity.getClass());
		}
		
		int len = ed.getColumn().size();
		
		Object[] args = new Object[len + 1];
		
		
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(ed.getTableName());
		sb.append(" SET ");
		
		try {
			for (int i = 0; i < len; i++) {
				EntityDesc.Column c = ed.getColumn().get(i);
				
				sb.append(c.getDbColumnName()).append(" = ?,");
				
				args[i] = PropertyUtils.getProperty(entity, c.getClazzProName());
				
			}
			args[len] = PropertyUtils.getProperty(entity, ed.getPrimaryKey());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" WHERE ").append(ed.getPrimaryKey()).append(" = ?");
		
		logger.debug(sb.toString());
		jdbcTemplate.update(sb.toString(), args);
		
	}
	
	
	
	public void delete(Object entity) {
		Class<?> clazz = entity.getClass();
		final EntityDesc ed = EntityReader.getEntityDesc(clazz);
		
		if(ed == null) {
			throw new RuntimeException("No such entity "+ entity.getClass());
		}
		
		StringBuilder sb = new StringBuilder("DELETE FROM ");
		sb.append(ed.getTableName());
		sb.append(" WHERE ");
		
		Object id = null;
		try {
			 sb.append(ed.getPrimaryKey()).append(" = ?");
			 id = PropertyUtils.getProperty(entity, ed.getPrimaryKey());
			 
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		
		logger.debug(sb.toString());
		jdbcTemplate.update(sb.toString(), new Object[]{id});
	}
	
	public <T> List<T> query(final Class<T> clazz,String condition, Map<String,Object> param) {
		final EntityDesc ed = EntityReader.getEntityDesc(clazz);
		if(ed == null)
			return null;
		
		StringBuilder sb = new StringBuilder("SELECT ");
		
		for(EntityDesc.Column c : ed.getColumn()) {
			sb.append(c.getDbColumnName()).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" FROM ")
			.append(ed.getTableName());
			
		
		if(StringUtils.isNotBlank(condition))
			sb.append(" WHERE ").append(condition);
			
		
		Map<String, Object> formatMap = new HashMap<String, Object>();
		
		String formatSql = sqlFormatter.formatSql(sb.toString(), param, formatMap,
				PARAM_IN_SEPERATOR);

		Object[] args = NamedParameterUtils.buildValueArray(formatSql,
				formatMap);
		
		
		logger.debug(formatSql + "=>" + formatMap);
		
		return jdbcTemplate.query(formatSql, args, new RowMapper<T>() {

			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				T t = null;
				try {
					t = clazz.newInstance();
					for(EntityDesc.Column c : ed.getColumn()) {
						Object value = rs.getObject(c.getDbColumnName());
						if(value == null)
							 continue;
						 
					  BeanUtils.setProperty(t, c.getClazzProName(), value);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					throw new RuntimeException(e);
				}
				return t;
			}

		});
	}
	
}
