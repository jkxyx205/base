package com.rick.base.dao;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public class Utils {
	public static  Map<String,Object> bean2Map(Object object)  {
		if(object == null)
            return Collections.emptyMap();

        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(object.getClass());
        Map<String,Object> objectMap = new HashMap<String,Object>(pds.length);

        
        try {
        	for(PropertyDescriptor pd: pds) {
                String name = pd.getName();

                Class<?> type = pd.getPropertyType();

                if (!(String[].class == type || String.class == type
                        ||Integer.class == type || Integer[].class == type
                        || Long.class == type || Long[].class == type
                        || Double.class == type || Double[].class == type)) {
                    continue;
                }

                Object value = null;
    			 
    			value = PropertyUtils.getProperty(object, name);
    			 

                if(value == null)
                    continue;

                if (String[].class == type) {
                    String[] arr = (String[])value;
                    StringBuilder sb = new StringBuilder();
                    for(String s : arr) {
                        sb.append(s).append(";");
                    }
                    objectMap.put(name,sb);
                } else if (Double[].class == type) {
                    Double[] arr = (Double[])value;
                    StringBuilder sb = new StringBuilder();
                    for(Double s : arr) {
                        sb.append(s).append(";");
                    }
                    objectMap.put(name,sb);
                } else if (Float[].class == type) {
                    Float[] arr = (Float[])value;
                    StringBuilder sb = new StringBuilder();
                    for(Float s : arr) {
                        sb.append(s).append(";");
                    }
                    objectMap.put(name,sb);
                } else if (Long[].class == type) {
                    Long[] arr = (Long[])value;
                    StringBuilder sb = new StringBuilder();
                    for(Long s : arr) {
                        sb.append(s).append(";");
                    }
                    objectMap.put(name,sb);
                }else {
                    objectMap.put(name,value);
                }
            }
        } catch(Exception e) {
        	throw new RuntimeException(e);
        }
        
        return objectMap;
		
	}
}
