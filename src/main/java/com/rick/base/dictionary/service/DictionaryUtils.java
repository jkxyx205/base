package com.rick.base.dictionary.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.rick.base.dictionary.bo.DicItem;
import com.rick.base.dictionary.bo.Dictionary;
import com.rick.base.dictionary.bo.ItemKey;
import com.rick.base.util.I18nUtil;

/** 
 * <description> 
 * 字典转换类
 * @author  xuxu
 * @datetime  Sep 18, 2013 11:40:18 AM
*/
public final class DictionaryUtils {
	
	private static Map<String,ItemKey> keyItemMap;
	
	private static Map<String,String> aliasMap;
	
	private static Dictionary dictionary;
	
	private DictionaryUtils() {}
	
	/**@description
	 * 字典转换
	 * @author:  xuxu
	 * @param o
	 * 		任意对象
	 * 		如：List<User> 、List<Map>
	 *         Map<String,String>、Map<String,User>
	 *         JSONObject JSONArray
	 *         普通Entity 等等
	 *      总之不管Object有多复杂，都会递归调用解析 
	 * @param igoreField 
	 * 		  被忽略解析的字段
	 * 		  常用于前台大部分字段展示，有个别字段需要编辑，那么该字段就需要放弃解析。
	 * @return void
	 * @throws Exception 
	*/
	public static void convertAnyThing(Object o) throws Exception {
		convertAnyThing(o,"");
	}
	
	@SuppressWarnings("unchecked")
	public static void convertAnyThing(Object o,String ... igoreField) throws Exception {
		if (o == null || o instanceof Number
				|| o.getClass() == Class.class || o.getClass() == String.class
				|| o.getClass() == Date.class)
			return;
		
		if(o instanceof List) {
			Iterable<?> it = (Iterable<?>)o;
			for(Object object : it) {
				convertAnyThing(object,igoreField);
			}
		} else if(o instanceof Map){
			Set<String> tableNameSet = null;
			tableNameSet = getTableNameSet(tableNameSet, igoreField);
			Set<?> entrySet = null;
			Map<Object, String> map = (Map<Object, String>)o;
			entrySet =  map.entrySet();
			
			Iterator<?> it = entrySet.iterator();
			while(it.hasNext()) {
				Entry<?,?> entry = (Entry<?,?>) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				if(key instanceof String) {
					String strKey = (String)key;
					if(tableNameSet != null && (tableNameSet.contains(strKey.toLowerCase()) || tableNameSet.contains(strKey.toUpperCase())))
						continue;
					
					if(value instanceof String && StringUtils.isNotBlank((String)value)) {
							map.put(key, convertString((String) key,(String) value));
					} else {
						convertAnyThing(value,igoreField);
					}
				}
			}
		} else { //实体对象
			PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(o);
			Set<String> tableNameSet = null;
			tableNameSet = getTableNameSet(tableNameSet, igoreField);
			
			for(PropertyDescriptor prop: props) {
				String propName  = prop.getDisplayName();
				if(tableNameSet != null && (tableNameSet.contains(propName.toLowerCase()) || tableNameSet.contains(propName.toUpperCase())))
					continue;
				
				Object value = PropertyUtils.getProperty(o, propName);
				if(value instanceof String && StringUtils.isNotBlank((String)value)) {
					PropertyUtils.setProperty(o, propName, convertString(propName,(String)value));
				} else {
					convertAnyThing(value,igoreField);
				}
			}
		}
	}

	/**@description
	 * @author:  xuxu
	 * @param 
	 * @return Set<String>
	*/
	private static Set<String> getTableNameSet(Set<String> tableNameSet,
			String... igoreField) {
		if(!(igoreField.length == 1 && StringUtils.isBlank(igoreField[0]))) {
			tableNameSet = new HashSet<String>(igoreField.length);
			CollectionUtils.addAll(tableNameSet, igoreField);
		}
		return tableNameSet;
	}
	
	private static String getKey(String prop) {
		for(Dictionary.Item item : dictionary.getItemList()) {
			if(item.getPatterns().contains(prop.toUpperCase()) || item.getPatterns().contains(prop.toLowerCase())) {
				return item.getKey();
			}
		}
		return null;
	}
	 
	/**@description
	 * @author:  xuxu
	 * @param pros
	 * 		属性：性别
	 * @param value
	 * 		存储值：1
	 * @return String {男}
	 * 		多级字典用#隔开
	*/
	public static String convertString(String pros,String value) {
		String key = getKey(pros);
		if(key == null)
				return value;
		
		ItemKey iz = keyItemMap.get(key);
		if(iz == null)
			return value;
		
		if(iz.isMultilevel()) { //多级字典
			List<String> list = new ArrayList<String>();
			Map<String,DicItem> map = new HashMap<String,DicItem>();
			DicItem t = null;
			for(DicItem d:iz.getItemList()) {
				map.put(d.getId(), d);
				if(d.getValue().equals(value)) {
					t = d;
					if(StringUtils.isBlank(t.getFid()))
						break;
				}
				
			}
			if(t != null) {
				while(StringUtils.isNotBlank(t.getFid())) {
					list.add(t.getTrueValue());
					t = map.get(t.getFid());
				}
				list.add(t.getTrueValue());
				Collections.reverse(list);
			} else {
				return value;
			}
			
			StringBuilder sb =new StringBuilder();
			int len = list.size();
			for(int i = 0; i<list.size(); i++) {
				sb.append(list.get(i));
				if(i<len-1)
					sb.append("#");
			}
			return sb.toString();
		} 
		
		return getTrueValueByKeyorAlias(key,value);
	}
	
	/**
	 * @description
	 * 根据索引和具体值获取对应的真实名称
	 * getDictionaryRealName("GB/T 2261.1-2003.0","1");
	 * @author:  xuxu
	 * @param key               字段索引              GB/T 2261.1-2003.0 或 xb(人员性别别名) 或 RYLKDW(人员列控单位) 前提是在dictionary.xml中配置过
	 * @param value             对应的具体项的值        1
	 * @return String			真实名称               未知的性别
	 */
	public static String getTrueValueByKeyorAlias(String key,String value) {
		String trueValue = null;
		Map<String, String> itemMap = getItemByKeyorAlias(key);
		if(itemMap != null)
			trueValue = itemMap.get(value) ; //解析字典
		return trueValue == null ? value : trueValue;
	}
	
	/**
	 * @description
	 * 根据索引查到对应的字段
	 * 例如人员性别：
	 * getDictionaryByIndex("GB/T 2261.1-2003.0");
	 * @author:  xuxu
	 * @param  index                   字段索引              GB/T 2261.1-2003.0
	 * @return Map<String,String>      对应的具体项           {"1":"未知的性别","2":"男性"};
	 */
	public static  Map<String,String> getItemByKeyorAlias(String key) {
		ItemKey itemKey = keyItemMap.get(key);
		Map<String, String> ret = null;
		if(itemKey != null)	
			ret = itemKey.getItemMap();
		else {
			key = aliasMap.get(key);
			itemKey = keyItemMap.get(key);
			if(itemKey == null)
				return ret;
			
			ret = itemKey.getItemMap();
		}
		
		Map<String, String> sMap = new HashMap<String,String>(ret.size());
		//國際化
		for(Map.Entry<String, String> kv : ret.entrySet()) {
			String key2 = kv.getKey();
			String value2 = kv.getValue();
			sMap.put(key2, I18nUtil.getMessageByCode(value2));
		}
		
		return sMap;
	}

	public static Dictionary getDictionary() {
		return dictionary;
	}

	static void setDictionary(Dictionary dictionary) {
		DictionaryUtils.dictionary = dictionary;
	}

	public static Map<String, ItemKey> getKeyItemMap() {
		return Collections.unmodifiableMap(keyItemMap);
	}

	static void setKeyItemMap(Map<String, ItemKey> keyItemMap) {
		DictionaryUtils.keyItemMap = keyItemMap;
	}

	public static Map<String, String> getAliasMap() {
		return Collections.unmodifiableMap(aliasMap);
	}

	static void setAliasMap(Map<String, String> aliasMap) {
		DictionaryUtils.aliasMap = aliasMap;
	}
}
