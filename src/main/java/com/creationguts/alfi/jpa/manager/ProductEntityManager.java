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

	public Product findByBarcode(String barcode) {
		Product p = getEntityManager()
				.createQuery("from Product where barcode = '" + barcode + "'",
						Product.class).getSingleResult();
		return p;
	}

	private static Logger logger = Logger.getLogger(ProductEntityManager.class);
}
