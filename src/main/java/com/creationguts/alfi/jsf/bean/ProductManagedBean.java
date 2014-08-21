package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.manager.ProductEntityManager;
import com.creationguts.alfi.jpa.vo.Product;

@ManagedBean
@SessionScoped
public class ProductManagedBean implements Serializable {

	@PostConstruct
	public void init() {
		product = new Product();
	}
	
	public String saveProduct() {

		ProductEntityManager productEntityManager = new ProductEntityManager();
		product = productEntityManager.save(product);
		logger.debug("Saving new product with barcode " + product.getBarcode());
		FacesContext.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage("Produto registrado com Id "
								+ product.getId()));

		product = new Product();
		return "edit_product";
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	private Product product;
	private static final long serialVersionUID = -5138184177418605947L;
	private static Logger logger = Logger.getLogger(ProductManagedBean.class);
}
