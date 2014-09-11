package com.creationguts.alfi.jpa.manager;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.vo.Purchase;

public class PurchaseEntityManager extends EntityManager<Purchase> {
	
	public PurchaseEntityManager() {
		super(Purchase.class);
	}
	
	public PurchaseEntityManager(Class<Purchase> clazz) {
		super(Purchase.class);
	}
	
	@Override
	public Purchase loadAll(Purchase purchase) {
		logger.debug("Loading all attributes from purchase");
		getEntityManager().getTransaction().begin();
		purchase = getEntityManager()
				.createQuery(
						"from Purchase p left join fetch p.purchaseProducts pp where p.id = "
								+ purchase.getId(), Purchase.class)
				.getSingleResult();
		getEntityManager().getTransaction().commit();
		closeEntityManager();
		return purchase;
	}

	private static Logger logger = Logger.getLogger(PurchaseEntityManager.class);


}
