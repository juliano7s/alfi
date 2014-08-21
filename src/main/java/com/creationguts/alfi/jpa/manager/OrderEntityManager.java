package com.creationguts.alfi.jpa.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.vo.Order;
import com.creationguts.alfi.jsf.bean.OrderSearchManagedBean;

public class OrderEntityManager extends EntityManager<Order> {
	
	public OrderEntityManager() {
		super(Order.class);
	}

	public OrderEntityManager(Class<Order> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	public List<Order> getOrders() {
		
		logger.debug("Getting orders");
		getEntityManager().getTransaction().begin();
		List<Order> result = getEntityManager().createQuery( "from Order", Order.class ).getResultList();
		logger.debug("total orders returned: " + result.size());
		getEntityManager().getTransaction().commit();
		getEntityManager().close();
	
		return result;
	}
	
	public List<Order> getOrders(OrderSearchManagedBean searchBean) {
		logger.debug("Getting orders by order search bean");
		String query = "from Order where 1=1";
		
		String requestDateCondition = "";
		if (searchBean.getRequestDateAfter() != null) {
			requestDateCondition += "requestDate >= :reqDateBegin";
			if (searchBean.getRequestDateBefore() != null) {
				requestDateCondition += " and requestDate <= :reqDateEnd";
			}
		} else {
			if (searchBean.getRequestDateBefore() != null) {
				requestDateCondition += "requestDate <= :reqDateEnd";
			}
		}
		if (!requestDateCondition.equals("")) {
			query += " and " + requestDateCondition;
		}
		
		String deliveryDateCondition = "";
		if (searchBean.getRequestDateAfter() != null) {
			deliveryDateCondition += "deliveryDate >= :dvryDateBegin";
			if (searchBean.getRequestDateBefore() != null) {
				deliveryDateCondition += " and deliveryDate <= :dvryDateEnd";
			}
		} else {
			if (searchBean.getRequestDateBefore() != null) {
				deliveryDateCondition += "deliveryDate <= :dvryDateEnd";
			}
		}
		if (!deliveryDateCondition.equals("")) {
			query += " and " + deliveryDateCondition;
		}
		
		String valueCondition = "";
		if (searchBean.getValueLesser() != 0.0) {
			valueCondition += "value >= :valueLesser";
			if (searchBean.getValueGreater() != 0.0) {
				valueCondition += " and value <= :valueGreater";
			}
		} else {
			if (searchBean.getValueGreater() != 0.0) {
				valueCondition += "value <= :valueGreater";
			}
		}
		if (!valueCondition.equals("")) {
			query += " and " + valueCondition;
		}
		
		String costCondition = "";
		if (searchBean.getValueLesser() != 0.0) {
			costCondition += "cost >= :costLesser";
			if (searchBean.getValueGreater() != 0.0) {
				costCondition += " and cost <= :costGreater";
			}
		} else {
			if (searchBean.getValueGreater() != 0) {
				costCondition += "cost <= :costGreater";
			}
		}
		if (!costCondition.equals("")) {
			query += " and " + costCondition;
		}
		
		String descriptionCondition = "";
		if (searchBean.getDescription() != null && !searchBean.getDescription().equals("")) {
			descriptionCondition += "description like :desc";
		}
		if (!descriptionCondition.equals("")) {
			query += " and " + descriptionCondition;
		}

		String statusCondition = "";
		if (!searchBean.getStatus().getViewName().equals("")) {
			statusCondition += "status = ':status'";
		}
		if (!statusCondition.equals("")) {
			query += " and " + statusCondition;
		}
		logger.debug("query: " + query);
		
		TypedQuery<Order> q = getEntityManager().createQuery(query, Order.class);
		for (Parameter<?> p : q.getParameters()) {
			if (p.getName().equals("reqDateBegin")) {
				q.setParameter("reqDateBegin", searchBean.getRequestDateAfter(), TemporalType.DATE);
			}
			
			if (p.getName().equals("reqDateEnd")) {
				q.setParameter("reqDateEnd", searchBean.getRequestDateBefore(), TemporalType.DATE);
			}
			
			if (p.getName().equals("dvryDateBegin")) {
				q.setParameter("dvryDateBegin", searchBean.getDeliveryDateAfter(), TemporalType.DATE);
			}
			
			if (p.getName().equals("dvryDateEnd")) {
				q.setParameter("dvryDateEnd", searchBean.getDeliveryDateBefore(), TemporalType.DATE);
			}
			
			if (p.getName().equals("valueLesser")) {
				q.setParameter("valueLesser", searchBean.getValueLesser());
			}
			
			if (p.getName().equals("valueGreater")) {
				q.setParameter("valueGreater", searchBean.getValueGreater());
			}
			
			if (p.getName().equals("costLesser")) {
				q.setParameter("costLesser", searchBean.getCostLesser());
			}
			
			if (p.getName().equals("costGreater")) {
				q.setParameter("costGreater", searchBean.getCostGreater());
			}
			
			if (p.getName().equals("desc")) {
				q.setParameter("desc", "%" + searchBean.getDescription() + "%");
			}
			
			if (p.getName().equals("status")) {
				q.setParameter("status", searchBean.getStatus().toString());
			}
		}

		getEntityManager().getTransaction().begin();
		List<Order> orders = q.getResultList();
		logger.debug("total orders returned: " + orders.size());
		getEntityManager().getTransaction().commit();
		getEntityManager().close();

		return orders;
	}
	
	public Map<Date, List<Order>> getOrders(Date beginDeliveryDate,
			Date endDeliveryDate, Date beginReadyDate, Date endReadyDate,
			Order.Status... status) {
		
		logger.debug("Parameters passed: " + beginDeliveryDate + endDeliveryDate + beginReadyDate + endReadyDate);

		String orderBy = "";
		
		String rdyDate = "";
		if (beginReadyDate == null) {
			if (endReadyDate != null) {
				rdyDate = " and o.readyDate <= :endrdy";
				orderBy = " order by o.readyDate asc";
			}
		} else {
			if (endReadyDate != null)
				rdyDate = " and o.readyDate >= :beginrdy and o.readyDate <= :endrdy";
			else
				rdyDate = " and o.readyDate >= :beginrdy";
			orderBy = " order by o.readyDate asc";
		}
		
		String dvrDate = "";
		if (beginDeliveryDate == null) {
			if (endDeliveryDate != null) {
				dvrDate = " and o.deliveryDate <= :enddvry";
				orderBy = " order by o.deliveryDate asc";
			}
		} else {
			if (endDeliveryDate != null)
				dvrDate = " and o.deliveryDate >= :begindvry and o.deliveryDate <= :enddvry";
			else
				dvrDate = " and o.deliveryDate >= :begindvry";
			orderBy = " order by o.deliveryDate asc";
		}
		
		String statusQry = status.length > 0 ? " and (" : "";
		for (int i = 0; i < status.length; i++) {
			statusQry += " o.status = :status" + i;
			if (i + 1 < status.length)
				statusQry += " or ";
		}
		statusQry += status.length > 0 ? ")" : "";

		logger.debug("Getting orders");
		getEntityManager().getTransaction().begin();
		TypedQuery<Order> q = getEntityManager().createQuery(
				"from Order o where 1=1 " + dvrDate + rdyDate + statusQry + orderBy,
				Order.class);
		logger.debug(" query formed: " + q);
		
		for (Parameter<?> p : q.getParameters()) {
			if (p.getName().equals("begindvry")) {
				q.setParameter("begindvry", beginDeliveryDate, TemporalType.DATE);
			}
			if (p.getName().equals("enddvry")) {
				q.setParameter("enddvry", endDeliveryDate, TemporalType.DATE);
			}
			if (p.getName().equals("beginrdy")) {
				q.setParameter("beginrdy", beginReadyDate, TemporalType.DATE);
			}
			if (p.getName().equals("endrdy")) {
				q.setParameter("endrdy", endReadyDate, TemporalType.DATE);
			}
			for (int i = 0; i < status.length; i++) {
				if (p.getName().equals("status" + i)) {
					q.setParameter("status" + i, status[i]);
				}
			}
			logger.debug("Parameter " + p.getName() + " - " + q.isBound(p));
		}
		
		List<Order> result = q.getResultList();
		Map<java.util.Date, List<Order>> resultMap = new HashMap<java.util.Date, List<Order>>();
		
		for (Order o : result) {
			List<Order> ol = null;
			if (!resultMap.containsKey(o.getDeliveryDate())) {
				ol = new ArrayList<Order>();
				ol.add(o);
				resultMap.put(o.getDeliveryDate(), ol);
			} else {
				ol = resultMap.get(o.getDeliveryDate());
				ol.add(o);
			}
		}
		
		logger.debug("total orders returned: " + result.size());
		logger.debug("Result map: ");
		for (Date k : resultMap.keySet()) {
			logger.debug("Key Date " + k);
			for (Order o : resultMap.get(k)) {
				logger.debug("order id " + o.getId());
			}
		}
		getEntityManager().getTransaction().commit();
		getEntityManager().close();

		return resultMap;
	}

	@Override
	public Order loadAll(Order order) {
		return order;
	}
	
	private static Logger logger = Logger.getLogger(OrderEntityManager.class);

	
}
