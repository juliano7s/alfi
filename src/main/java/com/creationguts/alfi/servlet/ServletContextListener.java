package com.creationguts.alfi.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		javax.servlet.ServletContext context = event.getServletContext();
		System.setProperty("rootPath", context.getRealPath("/"));
	}

}
