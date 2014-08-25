package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import scala.actors.threadpool.Arrays;

import com.creationguts.alfi.jpa.manager.ClientEntityManager;
import com.creationguts.alfi.jpa.manager.OrderEntityManager;
import com.creationguts.alfi.jpa.manager.UserEntityManager;
import com.creationguts.alfi.jpa.vo.Client;
import com.creationguts.alfi.jpa.vo.Order;
import com.creationguts.alfi.jpa.vo.Order.Status;
import com.creationguts.alfi.jpa.vo.User;

@ManagedBean
@SessionScoped
public class OrderManagedBean implements Serializable {
	
	public Date getLateOrdersEnd() {
		return lateOrdersEnd;
	}

	public void setLateOrdersEnd(Date lateOrdersEnd) {
		this.lateOrdersEnd = lateOrdersEnd;
	}

	public Date getNextOrdersEnd() {
		return nextOrdersEnd;
	}

	public void setNextOrdersEnd(Date nextOrdersEnd) {
		this.nextOrdersEnd = nextOrdersEnd;
	}

	public Date getLateOrdersBegin() {
		return lateOrdersBegin;
	}

	public void setLateOrdersBegin(Date lateOrdersBegin) {
		this.lateOrdersBegin = lateOrdersBegin;
	}

	public Date getNextOrdersBegin() {
		return nextOrdersBegin;
	}

	public void setNextOrdersBegin(Date nextOrdersBegin) {
		this.nextOrdersBegin = nextOrdersBegin;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		logger.debug("initializing orderManagedBean");
		order = new Order();
		owners = (new UserEntityManager()).getUsers();
		statuses = new ArrayList<Order.Status>(Arrays.asList(Order.Status.values()));
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -7);
		lateOrdersBegin = c.getTime();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -1);
		lateOrdersEnd = c.getTime();
		nextOrdersBegin = new Date();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, 7);
		nextOrdersEnd = c.getTime();
		logger.debug("Init dates: " + lateOrdersBegin + " " + lateOrdersEnd
				+ " " + nextOrdersBegin + " " + nextOrdersEnd);
	}
	
	public String editOrder() {
		logger.debug("Editing order");
		Long orderId = Long.parseLong(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("orderId"));
		logger.debug("Id: " + orderId);
		String fromPage = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("fromPage");
		logger.debug("fromPage: " + fromPage);
		
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("fromPage", fromPage);
		order = (new OrderEntityManager()).findById(orderId);
		
		return "edit_order";
	}
	
	/**
	 * Action
	 * @return
	 */
	public String setOrderReady() {
		order.setStatus(Status.READY);
		saveOrder();
		return null;
	}
	
	/**
	 * Action
	 * @return
	 */
	public String saveOrder() {
		logger.debug("Saving new order: " + order);
		String fromPage = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("fromPage");
		logger.debug("fromPage: " + fromPage);
		String cId = FacesContext.getCurrentInstance()
		 		.getExternalContext().getRequestParameterMap().get("clientId");		
		
		if (!cId.equals("")) {
			ClientEntityManager cem = new ClientEntityManager();
			Client c = cem.findById(Long.parseLong(cId));
			order.setClient(c);				
		}	
		
		OrderEntityManager oem = new OrderEntityManager();
		
		for (User o : owners) {
			if (o.getId().equals(orderOwnerId)) {
				if (o.getName().equals("Nenhum"))
					order.setOwner(null);
				else
					order.setOwner(o);
			}
		}
		oem.save(order);
		
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage("Pedido salvo com sucesso para o cliente: "
						+ order.getClient().getName()));

		logger.debug("Order saved. Clearing order bean.");
		order = new Order();
		return fromPage;
	}
	
	public String loadLateOrders() {
		getLateOrders();
		return "index";
	}
	
	public String loadNextOrders() {
		getNextOrders();
		return "index";
	}
				
	public Order getOrder() {
		logger.debug("getting order " + order.hashCode() + " from object " + this);
		return order;
	}

	public void setOrder(Order order) {
		logger.debug("setting order " + order.hashCode() + " in object " + this);
		this.order = order;
	}
	
	public Long getOrderOwnerId() {
		return orderOwnerId;
	}

	public void setOrderOwnerId(Long orderOwnerId) {
		this.orderOwnerId = orderOwnerId;
	}

	public List<User> getOwners() {
		return owners;
	}

	public void setOwners(List<User> owners) {
		this.owners = owners;
	}

	public List<Order.Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<Order.Status> statuses) {
		this.statuses = statuses;
	}
	
	public List<Date> getLateOrdersKeys()  {
		return new ArrayList<Date>(getLateOrders().keySet());
	}
	
	public Map<Date, List<Order>> getLateOrders() {
		OrderEntityManager oem = new OrderEntityManager();
		if (lateOrders == null || lateOrders.size() <= 0) {
			lateOrders = oem.getOrders(lateOrdersBegin, lateOrdersEnd, null, null,
					Order.Status.INPROGRESS, Order.Status.READY);
		}
		
		return lateOrders;
	}

	public void setLateOrders(Map<Date, List<Order>> lateOrders) {
		this.lateOrders = lateOrders;
	}

	public List<Date> getNextOrdersKeys()  {
		return new ArrayList<Date>(getNextOrders().keySet());
	}
	
	public Map<Date, List<Order>> getNextOrders() {
		OrderEntityManager oem = new OrderEntityManager();
		if (nextOrders == null || nextOrders.size() <= 0) {
			nextOrders = oem.getOrders(nextOrdersBegin, nextOrdersEnd, null, null,
					Order.Status.INPROGRESS, Order.Status.READY);
		}
		return nextOrders;
	}

	public void setNextOrders(Map<Date, List<Order>> nextOrders) {
		this.nextOrders = nextOrders;
	}

	private Order order;
	private Long orderOwnerId;
	private List<User> owners;
	private List<Order.Status> statuses;
	private Map<Date, List<Order>> lateOrders;
	private Date lateOrdersBegin, lateOrdersEnd;
	private Date nextOrdersBegin, nextOrdersEnd;
	private Map<Date, List<Order>> nextOrders;

	private static final long serialVersionUID = 536149967322807306L;
	private static Logger logger = Logger.getLogger(OrderManagedBean.class);
}
