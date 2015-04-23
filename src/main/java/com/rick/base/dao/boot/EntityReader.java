package com.rick.base.dao.boot;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.rick.base.dao.boot.EntityDesc.Column;


public class EntityReader {
	
	private static final transient Logger logger = LoggerFactory
			.getLogger(EntityReader.class);
	
	private static final  ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
	
	private static final Map<Class<?>,EntityDesc> entityMap = new HashMap<Class<?>,EntityDesc>();
	
	public static EntityDesc getEntityDesc(Class<?> clazz) {
		return entityMap.get(clazz);
	}
	
	static void scanningCandidateEntity(List<String> packagesList) throws NoSuchFieldException, SecurityException {
		for (String pg : packagesList) {
			scanningCandidateEntity(pg);
		}
	}
	
	static void scanningCandidateEntity(String pg) throws NoSuchFieldException, SecurityException {
		provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		Set<BeanDefinition> set = provider.findCandidateComponents(pg);
		
		if(CollectionUtils.isEmpty(set)) 
			return;
		
		for (BeanDefinition bean : set) {
			try {
				Class<?> clazz = Class.forName(bean.getBeanClassName());
				logger.info("load class " + clazz);
				analysis(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}
	
	/**
	 * @param clazz
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	static void analysis(Class<?> clazz) throws NoSuchFieldException, SecurityException {
		Entity entity = clazz.getAnnotation(Entity.class);
		
		String tableName = entity.name();
		
		EntityDesc ed = new EntityDesc();
		
		ed.setTableName(StringUtils.isBlank(tableName) ? clazz.getSimpleName() : tableName);
		
		//获取属性
		Field[] fileds = clazz.getDeclaredFields();
		
		for (Field field : fileds) {
			field.setAccessible(true);
			
			Annotation[] anns = field.getAnnotations();
			if(anns.length > 0) {
				for (Annotation ann : anns) {
					if(ann.annotationType() == Transient.class) 
						continue;
					
					if(ann.annotationType() == GeneratedValue.class) {
						ed.setType(((GeneratedValue) ann).strategy());
						continue;
					}
					
					if(ann.annotationType() == Id.class) {
						ed.setPrimaryKey(field.getName());
					}
					
					//column
					Column c = new Column();
					
					if(ann.annotationType() == javax.persistence.Column.class) {
						javax.persistence.Column cn = (javax.persistence.Column)ann;
						c.setColumnDefinition(cn.columnDefinition());
						c.setLength(cn.length());
						c.setNullable(cn.nullable());
						c.setUnique(cn.unique());
						c.setDbColumnName(cn.name());
						c.setScale(cn.scale());
						c.setPrecision(cn.precision());
					}
				
					c.setClazzProName(field.getName());
					c.setClazzProType(field.getType());
					c.setDbColumnName(StringUtils.isBlank(c.getDbColumnName()) ? field.getName() : c.getDbColumnName());
					
					
					//
					 
					ed.getColumn().add(c);
				}
			} else {
				Column c = new Column();
				//column
				c.setClazzProName(field.getName());
				c.setClazzProType(field.getType());
				c.setDbColumnName(field.getName());
				ed.getColumn().add(c);
			}
		}
		
		if(ed.getType() == null)
			ed.setType(GenerationType.AUTO);
		
		entityMap.put(clazz, ed);
	}
}

