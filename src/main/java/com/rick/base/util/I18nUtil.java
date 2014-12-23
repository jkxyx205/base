package com.rick.base.util;

import org.springframework.context.i18n.LocaleContextHolder;

import com.rick.base.context.SpringInit;

public class I18nUtil {

	public static String getMessageByCode(String messageCode) {
		return getMessageByCode(messageCode,null);
	}
	
	public static String getMessageByCode(String messageCode,Object[] param) {
		return SpringInit.getSpringContext().getMessage(messageCode, param, LocaleContextHolder.getLocale());
	}
}
