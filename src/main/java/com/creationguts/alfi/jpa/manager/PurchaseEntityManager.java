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
		return purchase;
	}

	private static Logger logger = Logger.getLogger(PurchaseEntityManager.class);


}
