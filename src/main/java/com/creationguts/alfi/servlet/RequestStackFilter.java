package com.creationguts.alfi.servlet;

import java.io.IOException;
import java.util.Stack;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter//(urlPatterns="/*")
public class RequestStackFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		@SuppressWarnings("unchecked")
		Stack<String> requestStack = (Stack<String>) req.getSession().getAttribute("request-stack");
		if (requestStack == null) {
			requestStack = new Stack<String>();
		}
		//TODO got to push only one entry for each page.
		requestStack.push(req.getRequestURI());
		req.getSession().setAttribute("request-stack", requestStack);
		System.out.println("request: " + req);
		System.out.println("requestStack: " + requestStack);
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
