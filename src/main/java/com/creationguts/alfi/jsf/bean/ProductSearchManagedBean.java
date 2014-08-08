package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.manager.ProductEntityManager;
import com.creationguts.alfi.jpa.vo.Product;

@ManagedBean
@RequestScoped
public class ProductSearchManagedBean implements Serializable {


	@PostConstruct
	public void init() {
		barcode = "";
		description = "";
		foundProduct = null;
		foundProducts = null;
	}
	
	public String searchProduct() {
		ProductEntityManager pem = new ProductEntityManager();
		String page = "list_product";
		
		if (description.trim().equals("")) {
			if (!barcode.trim().equals("")) {
				foundProduct = pem.findByBarcode(barcode);
			}
		} else {
			foundProducts = pem.findByDescription(description);
		}
		
		if (foundProduct != null || (foundProducts != null && foundProducts.size() == 1)) {
			Product product = foundProduct == null ? foundProducts.get(0) : foundProduct;
			productManagedBean.setProduct(product);
			page = "edit_product";
		}
		
		return page;
	}
	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Product> getFoundProducts() {
		return foundProducts;
	}

	public void setFoundProducts(List<Product> foundProducts) {
		this.foundProducts = foundProducts;
	}

	public Product getFoundProduct() {
		return foundProduct;
	}

	public void setFoundProduct(Product foundProduct) {
		this.foundProduct = foundProduct;
	}

	private String barcode;
	private String description;
	private List<Product> foundProducts;
	private Product foundProduct;
	
	@ManagedProperty(value="#{productManagedBean}")
	private ProductManagedBean productManagedBean;

	
	private static final long serialVersionUID = 1619203277603284655L;
	private static Logger logger = Logger.getLogger(ProductSearchManagedBean.class);
}
