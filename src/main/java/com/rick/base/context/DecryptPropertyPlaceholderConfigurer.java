package com.rick.base.context;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.rick.base.util.DESUtils;

public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{  
    /** 
     * 重写父类方法，解密指定属性名对应的属性值 
     */  
    @Override  
    protected String convertProperty(String propertyName,String propertyValue){  
        if(isEncryptPropertyVal(propertyName)){  
            return DESUtils.getDecryptString(propertyValue);//调用解密方法  
        }else{  
            return propertyValue;  
        }  
    }  
    /** 
     * 判断属性值是否需要解密，这里我约定需要解密的属性名用encrypt开头 
     * @param propertyName 
     * @return 
     */  
    private boolean isEncryptPropertyVal(String propertyName){  
        if(propertyName.startsWith("encrypt")){  
            return true;  
        }else{  
            return false;  
        }  
    }  
}  