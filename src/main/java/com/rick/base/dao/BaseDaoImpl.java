package com.rick.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.rick.base.test.ControllerTest;

/**
 * @author Rick.Xu
 *
 */
@Repository("baseDao")
public class BaseDaoImpl {
	private static final transient Logger logger = LoggerFactory.getLogger(ControllerTest.class);  
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Resource(name="hibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
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
		SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			//e.printStackTrace();
			logger.info(e.getMessage());
		}
		if(session == null) {
			session = sessionFactory.openSession();
			session.setFlushMode(FlushMode.MANUAL);			
		}
		return session;
	}
	
/*	public void closeSession(Session session) {
		SessionFactoryUtils.closeSession(session);
	}*/
	
	/**
	 * just for oracle
	 * @param data
	 * @param tableName
	 */
	public void addMap(Map<String,Object> data, String tableName) {
		if(null == tableName)
			throw new NullPointerException("表名不能为空！");
		
		Map<String,String> columnMap = getColumnMap(tableName);
		
		final List<Simple> param = new ArrayList<Simple>();
		
		List<Simple> blobList = null;
		
		StringBuffer valueSQL = new StringBuffer(" VALUES (");
		
		StringBuffer insertSQL = new StringBuffer("INSERT INTO ").append(tableName).append("(");
		Set<Entry<String,Object>> entrySet = data.entrySet();
		Iterator<Entry<String,Object>> it = entrySet.iterator();
		
		while(it.hasNext()) {
			Entry<String,Object> entry = it.next();
			String key = String.valueOf(entry.getKey()).toLowerCase();
			
			if(columnMap.containsKey(key) || columnMap.containsKey((key = key.toUpperCase()))) {
				Object value = entry.getValue();
				if(value == null || "".equals(value))
					continue;
				insertSQL.append(key).append(",");
				String colunmType = (String) columnMap.get(key);
				
				if("BLOB".equals(colunmType)) {
    				if(null == blobList)
    					blobList =  new ArrayList<Simple>();
    				blobList.add(new Simple(value, key));
    			}
				param.add(new Simple(value, colunmType));
			 
				valueSQL.append("?").append(",");
				
			}
		}
		insertSQL.deleteCharAt(insertSQL.length()-1);
		insertSQL.append(")");
		
		valueSQL.deleteCharAt(valueSQL.length()-1);
		
		insertSQL.append(valueSQL).append(")");
		
		jdbcTemplate.update(insertSQL.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement pstmt) throws SQLException {
				setPstmt(pstmt,param);
			}
			
		});
		
	}

	/**
	 * just for oracle
	 * @param data
	 * @param tableName
	 * @param keys
	 */
	public void updateMap(Map<String,Object> data, String tableName, String[] keys) {
		if(null == tableName)
			throw new NullPointerException("表名不能为空！");
		
		StringBuffer updateSQL = new StringBuffer("UPDATE ").append(tableName).append(" SET ");
		Map<String, String> columnMap = getColumnMap(tableName);
		
		List<Simple> blobList = null;
		final List<Simple> param = new ArrayList<Simple>();
		
		Set<Entry<String,Object>> entrySet = data.entrySet();
		Iterator<Entry<String,Object>> it = entrySet.iterator();
		
		while(it.hasNext()) {
			Entry<String,Object> entry = it.next();
    		String key = String.valueOf(entry.getKey()).toUpperCase();
    	 
    		if(columnMap.containsKey(key) || columnMap.containsKey((key = key.toLowerCase()))) {
    			Object value = entry.getValue();
    			updateSQL.append(key).append(" = ");
    			String colunmType = (String) columnMap.get(key);
    			
    			if("BLOB".equals(colunmType)) {
    				if(null == blobList)
    					blobList =  new ArrayList<Simple>();
    				blobList.add(new Simple(value, key));
    			}
				param.add(new Simple(value, colunmType));
    			
    			updateSQL.append("?").append(",");
    			
    		}
		}
		
		updateSQL.deleteCharAt(updateSQL.length()-1);
		
		updateSQL.append(" WHERE ");
		int length = keys.length;
		for(int i = 0 ; i< keys.length ; i++) {
			String key = keys[i].toUpperCase();
			Object keyvalue = data.get(key);
			keyvalue = keyvalue == null ? data.get(key.toLowerCase()) : keyvalue;
			String colunmType = (String) columnMap.get(key);
			updateSQL.append(key).append(" = ").append(columnMapping(keyvalue,colunmType));
			if(i<length-1)
				updateSQL.append(" AND ");
		}
		
		//执行SQL
		jdbcTemplate.update(updateSQL.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement pstmt) throws SQLException {
				setPstmt(pstmt,param);
			}
		});
				
	}
	
	/**
	 * 目前获取oracle的数据结构
	 * @param tableName
	 * @return
	 */
	private Map<String,String> getColumnMap(String tableName) {
		//获取表所有字段
		String sql = "SELECT COLUMN_NAME,DATA_TYPE FROM USER_TAB_COLS  where table_name='"+tableName.toUpperCase()+"'";
		
		final Map<String, String> ret = new HashMap<String, String>();
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				ret.put(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE"));
			}
			
		});

		return ret;
	}
	
    private class Simple {
		private Object obj;
		
		private String str;

		public Simple(Object obj, String str) {
			this.obj = obj;
			this.str = str;
		}
	}
    
    private final void setPstmt(PreparedStatement pstmt, List<Simple> param) throws SQLException {
		int index = 1;
		for(int i = 0;i<param.size(); i++) {
			Simple s = (Simple) param.get(i);
			Object value = s.obj;
			if(value == null) {
				pstmt.setObject(index, value);
				index++;
				continue;
			}
				
			String colunmType = s.str;
			if("VARCHAR2".equals(colunmType) ||"CHAR".equals(colunmType)) {
				pstmt.setString(index, (String) value);
			} else if("NUMBER".equals(s.str)) {
				pstmt.setString(index, String.valueOf(value));
			} else if("BLOB".equals(s.str)) {
				pstmt.setBytes(index, (byte[]) value);
			} else if("DATE".equals(s.str)) {
			    pstmt.setTimestamp(index, new java.sql.Timestamp(((Date)value).getTime()));
			}else {
				pstmt.setObject(index, value);
			}
			index++;
		}
	}
	
	/**
	 * 字段字符串映射
	 * @return
	 */
	private String columnMapping(Object obj, String colunmType) {
		if(null == obj || "".equals(obj))
			return "null";
		else if(obj.getClass() == String.class && obj.toString().startsWith("ora_")){ //数据库函数
			return obj.toString().substring(4);
		} else if("NUMBER".equals(colunmType)) {
	    	return obj.toString();
	    } else if("VARCHAR2".equals(colunmType)) {
	    	return "'"+obj.toString()+"'";
	    }
		return obj.toString();
	}
	
	
}
