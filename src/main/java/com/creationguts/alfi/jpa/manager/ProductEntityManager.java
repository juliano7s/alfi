package com.creationguts.alfi.jpa.manager;

import java.util.List;

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

	public List<Product> findByDescription(String description) {
		List<Product> prods = getEntityManager().createQuery(
				"from Product where description like '%" + description + "'",
				Product.class).getResultList();
		return prods;
	}

	private static Logger logger = Logger.getLogger(ProductEntityManager.class);
}
