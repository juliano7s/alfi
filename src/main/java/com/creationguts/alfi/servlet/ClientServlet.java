package com.creationguts.alfi.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.manager.ClientEntityManager;
import com.creationguts.alfi.jpa.manager.OrderEntityManager;

/**
 * Servlet implementation class ClientServlet
 */
public class ClientServlet extends HttpServlet {
	   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("Calling servlet ClientServlet");
		ClientEntityManager cem = new ClientEntityManager();
		cem.getClients();
		OrderEntityManager oem = new OrderEntityManager();
		oem.getOrders();
	}
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ClientServlet.class);
}
