package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.manager.OrderEntityManager;
import com.creationguts.alfi.jpa.vo.Order;

@ManagedBean
@SessionScoped
public class OrderSearchManagedBean implements Serializable {
	
	public String searchOrder() {
		ordersFound = (new OrderEntityManager()).getOrders(this);
		String goTo = "list_orders";
		if (ordersFound.size() == 0) {
			goTo = "search_orders";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Nenhum pedido encontrado."));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(ordersFound.size() + " pedidos encontrados."));
		}
			
		return goTo;
	}
	
	public String editOrder() {
		logger.debug("Editing order");
		ExternalContext exCont = FacesContext.getCurrentInstance().getExternalContext();
		Long orderId = Long .parseLong(exCont.getRequestParameterMap().get("orderId"));
		logger.debug("orderId: " + orderId);
		String fromPage = exCont.getRequestParameterMap().get("fromPage");
		logger.debug("fromPage: " + fromPage);
		exCont.getRequestMap().put("fromPage", fromPage);
		Order order = (new OrderEntityManager()).findById(orderId);
		order = (new OrderEntityManager()).loadAll(order);
		logger.debug("order: " + order);
		logger.debug("setting order " + order.hashCode() + " on " + orderManagedBean);
		orderManagedBean.setOrder(order);
		return "edit_order";
	}

	public Float getValueGreater() {
		return valueGreater;
	}
	public void setValueGreater(Float valueGreater) {
		this.valueGreater = valueGreater;
	}
	public Float getValueLesser() {
		return valueLesser;
	}
	public void setValueLesser(Float valueLesser) {
		this.valueLesser = valueLesser;
	}
	public Float getCostGreater() {
		return costGreater;
	}
	public void setCostGreater(Float costGreater) {
		this.costGreater = costGreater;
	}
	public Float getCostLesser() {
		return costLesser;
	}
	public void setCostLesser(Float costLesser) {
		this.costLesser = costLesser;
	}
	public Date getRequestDateBefore() {
		return requestDateBefore;
	}
	public void setRequestDateBefore(Date requestDateBefore) {
		this.requestDateBefore = requestDateBefore;
	}
	public Date getRequestDateAfter() {
		return requestDateAfter;
	}
	public void setRequestDateAfter(Date requestDateAfter) {
		this.requestDateAfter = requestDateAfter;
	}
	public Date getDeliveryDateBefore() {
		return deliveryDateBefore;
	}
	public void setDeliveryDateBefore(Date deliveryDateBefore) {
		this.deliveryDateBefore = deliveryDateBefore;
	}
	public Date getDeliveryDateAfter() {
		return deliveryDateAfter;
	}
	public void setDeliveryDateAfter(Date deliveryDateAfter) {
		this.deliveryDateAfter = deliveryDateAfter;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Order.Status getStatus() {
		return status;
	}
	public void setStatus(Order.Status status) {
		this.status = status;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public List<Order> getOrdersFound() {
		return ordersFound;
	}

	public void setOrdersFound(List<Order> ordersFound) {
		this.ordersFound = ordersFound;
	}

	public OrderManagedBean getOrderManagedBean() {
		return orderManagedBean;
	}

	public void setOrderManagedBean(OrderManagedBean orderManagedBean) {
		this.orderManagedBean = orderManagedBean;
	}

	private Float valueGreater;
	private Float valueLesser;
	private Float costGreater;
	private Float costLesser;
	private Date requestDateBefore;
	private Date requestDateAfter;
	private Date deliveryDateBefore;
	private Date deliveryDateAfter;
	private String description;
	private Order.Status status;
	private Long ownerId;
	private List<Order> ordersFound;
	
	@ManagedProperty(value = "#{orderManagedBean}")
	private OrderManagedBean orderManagedBean;
	
	private static final long serialVersionUID = 4555862555695798169L;
	private static Logger logger = Logger.getLogger(OrderSearchManagedBean.class);
}
