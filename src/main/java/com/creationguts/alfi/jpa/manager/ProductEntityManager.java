package com.creationguts.alfi.jpa.manager;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.vo.Product;

public class ProductEntityManager extends EntityManager<Product> {
	
	public ProductEntityManager() {
		super(Product.class);
	}
	
	public ProductEntityManager(Class<Product> clazz) {
		super(clazz);
	}

	private static Logger logger = Logger.getLogger(ProductEntityManager.class);
}
