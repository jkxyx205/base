package com.rick.base.dao.client;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.dao.BaseDaoImpl.JdbcTemplateExecutor;
import com.rick.base.util.ServletContextUtil;

@Service
class ClientService {
	@Resource(name = "baseDao")
	private BaseDaoImpl dao;
	
	GridData getResultSet(String queryName,HttpServletRequest request) {
		
		GridData gd = new GridData();
		
		Map<String,Object> param = ServletContextUtil.getMap(true, request);
		
		long total = dao.queryForSpecificParamCount(queryName, param,new JdbcTemplateExecutor<Long>() {

			public Long query(JdbcTemplate jdbcTemplate, String queryString,
					Object[] args) {
				queryString = dao.formatSqlCount(queryString);
				return jdbcTemplate.queryForObject(queryString, args, Long.class);
			}
		}); 
		
		List<Map<String, Object>> rows = dao.queryForSpecificParam(queryName, param, new JdbcTemplateExecutor<List<Map<String, Object>>>() {

			public List<Map<String, Object>> query(JdbcTemplate jdbcTemplate,
					String queryString, Object[] args) {
				List<Map<String, Object>>  ret = jdbcTemplate.queryForList(queryString, args);
				return ret;
				
			}
		});
		
		gd.setTotal(total);
		gd.setDataList(rows);
		return gd;
		
	}

	public void executeSQL(String queryName, HttpServletRequest request) throws Exception {
		Map<String,Object> param = ServletContextUtil.getMap(true, request);
		dao.executeForSpecificParam(queryName, param);
	} 
}
