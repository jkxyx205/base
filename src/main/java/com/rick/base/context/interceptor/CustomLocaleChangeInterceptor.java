package com.rick.base.context.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

public class CustomLocaleChangeInterceptor extends LocaleChangeInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws ServletException {
		return super.preHandle(request, response, arg2);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		request.setAttribute("lang", LocaleContextHolder.getLocale());
		super.afterCompletion(request, response, handler, ex);
	}
	
}
