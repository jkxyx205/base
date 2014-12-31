package com.rick.base.dictionary.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.rick.base.dictionary.bo.Dictionary;


class DictionaryXmlReader {
	public Dictionary readDictionaryXml(File folder) throws Exception {
		return readDictionaryXml(null,folder);
	}
	/**
	 * @description
	 * @author:  xuxu
	 * @param xmlPath 文件路径
	 * @return Dictionary
	 * @throws Exception 
	 */
	public Dictionary readDictionaryXml(Dictionary dic,File folder) throws Exception {
		if(folder.isFile())
			throw new RuntimeException("字典配置文件目录不是目录！");
		if(dic == null)
		   dic = new Dictionary();
		
		File[] filelist = folder.listFiles();
		
		List <Dictionary.Item> itemList = new ArrayList<Dictionary.Item>(); 
		
		for(File f: filelist) {
			if(f.isDirectory())
				readDictionaryXml(dic,f);
			else {
				Document doc = null;
				try {
					SAXReader builder = new SAXReader();
					doc = builder.read(f);
				}catch (Exception e) {
					e.printStackTrace();
					throw e;
				} 
				Element root = doc.getRootElement();
				
				 for (Iterator<?> i = root.elementIterator(); i.hasNext(); ) {
					Element eitem = (Element)i.next();
					if(eitem.getName().equals("standard-sql")) {
						dic.setStandardSql(eitem.getText());
					}  else {
						Dictionary.Item ditem= dic.new Item();
						 for (Iterator<?> oo = eitem.elementIterator(); oo.hasNext(); ) {
							Element e = (Element)oo.next();
							 
							if(e.getName().equals("description")) {
								ditem.setDescription(e.getText());
							} else if(e.getName().equals("key")) {
								Attribute attr = e.attribute("alias");
								ditem.setKey(e.getText());
								if(attr != null && !StringUtils.isBlank(attr.getValue())) {
									ditem.setAlias(attr.getValue());
								}
							} else if(e.getName().equals("column-pattern")) {
								if(ditem.getPatterns() == null) {
									List<String> list = new ArrayList<String>();
									list.add(e.getText());
									ditem.setPatterns(list);
								} else {
									ditem.getPatterns().add(e.getText());
								}
							} else if(e.getName().equals("map")) { //自定义
								Map<String,String> map = null;
								for (Iterator<?> ooo = e.elementIterator(); ooo.hasNext(); ) {
									if(map == null)
										map = new LinkedHashMap<String,String>();
									Element ee = (Element)ooo.next();
									Attribute attr = ee.attribute("key");
									map.put(attr.getValue(), ee.getText());
								}
								ditem.setMap(map);
							}
						 
						}
						itemList.add(ditem);
					} 
			}
			}
		}
		dic.setItemList(itemList);
		return dic;
	}
}
