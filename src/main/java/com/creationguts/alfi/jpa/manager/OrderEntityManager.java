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

import com.creationguts.alfi.jpa.model.Order;

public class OrderEntityManager extends EntityManager<Order> {
	
	public List<Order> getOrders() {
		
		logger.debug("Getting orders");
		getEntityManager().getTransaction().begin();
		List<Order> result = getEntityManager().createQuery( "from Order", Order.class ).getResultList();
		logger.debug("total orders returned: " + result.size());
		getEntityManager().getTransaction().commit();
		getEntityManager().close();
	
		return result;
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
	
	private static Logger logger = Logger.getLogger(OrderEntityManager.class);
}
