package com.rick.base.context.filter.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.rick.base.context.SpringInit;
import com.rick.base.context.filter.auth.service.AuthService;

public class AuthFilter implements Filter {
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		WrapperResponse wrapper = new WrapperResponse((HttpServletResponse) response,request.getCharacterEncoding());
		chain.doFilter(request, wrapper);
		
		//replace
		String content = wrapper.getContent();
		
		AuthService service = SpringInit.getSpringContext().getBean(AuthService.class);
		content = service.html(content);
		
		response.getWriter().write(content);
	    
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
