package com.creationguts.alfi.jpa.manager;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.vo.Product;

public class ProductEntityManager extends EntityManager<Product> {

	public ProductEntityManager() {
		super(Product.class);
	}

	public ProductEntityManager(Class<Product> clazz) {
		super(clazz);
	}

	public Product findByBarcode(String barcode) throws NoResultException {
		Product p = getEntityManager()
				.createQuery("from Product where barcode = '" + barcode + "'",
						Product.class).getSingleResult();
		return p;
	}

	public List<Product> findByDescription(String description) {
		logger.debug("Finding products by description: " + description);
		List<Product> prods = getEntityManager().createQuery(
				"from Product where description like '%" + description + "%'",
				Product.class).getResultList();
		logger.debug(prods.size() + " products found.");
		return prods;
	}
	
	@Override
	public Product loadAll(Product product) {
		return product;
	}

	private static Logger logger = Logger.getLogger(ProductEntityManager.class);
}
