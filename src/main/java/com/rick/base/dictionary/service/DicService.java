package com.rick.base.dictionary.service;

import java.io.File;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rick.base.context.Constants;
import com.rick.base.dao.BaseDaoImpl;
import com.rick.base.dictionary.DictionaryUtils;
import com.rick.base.dictionary.bo.DicItem;
import com.rick.base.dictionary.bo.Dictionary;
import com.rick.base.dictionary.bo.ItemKey;

@Service
public class DicService {
	private static final transient Logger logger = LoggerFactory.getLogger(DicService.class);  
	
	@Resource
	private BaseDaoImpl baseDao;
	
	private String getQuerySql(StringBuilder sql, Dictionary dic) {
		boolean flag = false;
		for(Dictionary.Item item :dic.getItemList()) {
			if(item.getKey() != null) {
				if(!flag) {
					flag = true;
					sql.append(" WHERE KEY = '" + item.getKey() +"'");
				} else
					sql.append(" OR KEY = '" + item.getKey() +"'");
			} 
			 
		}
		
		sql.append(" ORDER BY KEY,SEQ");
		return sql.toString();
	}
 
	/**@description
	 * 初始化字典环境
	 * @author:  xuxu
	 * @param 
	 * @return void
	 * @throws Exception 
	*/
	public void initEnv() throws Exception  {
		initEnv(Constants.getInstance().DICTIONARY_PROPERTIES_FOLDER,Constants.getInstance().DICTIONARY_JS_FILE);
	}
	public void initEnv(String xmlPath,String jsPath) throws Exception  {
		
		DictionaryXmlReader reader = new DictionaryXmlReader();
		Dictionary dic = reader.readDictionaryXml(new File(xmlPath));
		
		Map<String,ItemKey> keyItemMap = new HashMap<String, ItemKey>();
		Map<String,String> aliasMap = new HashMap<String, String>();
		Map<String,Map<String, String>> jsData = null;
		if(jsPath != null)
			jsData = new HashMap<String, Map<String,String>>();
		
		
		//查询数据库字典
		StringBuilder sql = new StringBuilder(dic.getStandardSql()); 
		//对数据库字典进行映射
		try {
			
			List<Map<String, Object>> list = baseDao.getJdbcTemplate().queryForList(getQuerySql(sql,dic));
			for(Map<String, Object> map: list) {
				logger.debug("dictionary:|-----" + map.get("KEY"));
				ItemKey itemKey = keyItemMap.get(map.get("KEY"));
				Map<String,String> itemMap = null;
				if(itemKey == null) {
					itemKey = new ItemKey();
					keyItemMap.put((String)map.get("KEY"), itemKey);
					
					itemMap = new HashMap<String, String>(); 
					jsData.put((String)map.get("KEY"), itemMap);
				} else {
					itemMap = jsData.get(map.get("KEY"));
				}
				
				DicItem dicItem = new DicItem();
				dicItem.setId((String)map.get("ID"));
				dicItem.setKey((String)map.get("KEY"));
				dicItem.setTrueValue((String)map.get("TRUEVALUE"));
				dicItem.setValue((String)map.get("VALUE"));
				dicItem.setFid((String)map.get("FID"));
				
				itemKey.getItemList().add(dicItem);
				if(!itemKey.isMultilevel() && StringUtils.isNotBlank(dicItem.getFid())) {
					itemKey.setMultilevel(true);
				}
				itemKey.getItemMap().put((String)map.get("VALUE"), (String)map.get("TRUEVALUE"));
				logger.debug("dictionary:|-------------" + map.get("VALUE")+ ":" + map.get("TRUEVALUE"));
				
				itemMap.put((String)map.get("VALUE"), (String)map.get("TRUEVALUE"));
			}
			
			for(Dictionary.Item item :dic.getItemList()) {
				logger.debug("dictionary:|-----" + item.getKey());
				//对自定义字典映射start
				if(item.getMap() != null) {
					Set<Map.Entry<String, String>> entry = item.getMap().entrySet();
					ItemKey itemKey = new ItemKey();
					for (Iterator<Map.Entry<String, String>> iterator = entry.iterator(); iterator
							.hasNext();) {
						Map.Entry<String, String> me = iterator.next();
						
						DicItem dicItem = new DicItem();
						
						dicItem.setKey(item.getKey());
						dicItem.setTrueValue(me.getValue());
						dicItem.setValue(me.getKey());
						
						itemKey.getItemList().add(dicItem);
						itemKey.getItemMap().put(me.getKey(), me.getValue());
						logger.debug("dictionary:|-------------" + me.getKey( )+ ":" + me.getValue());
					}
					keyItemMap.put(item.getKey(), itemKey);
					
				}
				//对自定义字典映射end
				
				String aligns = item.getAlias();
				if(aligns == null || item.getKey() == null)
					continue;
				
				aliasMap.put(aligns, item.getKey());
			 
			}
			if(jsPath != null) {
				File f2 = new File(jsPath);
				ObjectMapper mapper = new ObjectMapper();  
		          
			     String Json =  mapper.writeValueAsString(jsData);  
			     String Json2 =  mapper.writeValueAsString(aliasMap);  
				//生成js文件 用于解析
				FileUtils.writeStringToFile(f2, 
						"var dicMap = " + Json + ";" +
						"var aligns = "+Json2+"",
						"utf-8");
			}
			  
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		DictionaryUtils.setDictionary(dic);
		DictionaryUtils.setKeyItemMap(keyItemMap);
		DictionaryUtils.setAliasMap(aliasMap);	}
	
}
