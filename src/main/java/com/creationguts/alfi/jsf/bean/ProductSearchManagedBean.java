package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.manager.ProductEntityManager;
import com.creationguts.alfi.jpa.vo.Product;

@ManagedBean
@SessionScoped
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
		String page = "list_products";

		try {
			if (barcode.trim().equals("")) {
				if (!description.trim().equals("")) {
					foundProducts = pem.findByDescription(description);
				}
			} else {
				foundProduct = pem.findByBarcode(barcode);
			}

			if (foundProduct != null
					|| (foundProducts != null && foundProducts.size() == 1)) {
				Product product = foundProduct == null ? foundProducts.get(0)
						: foundProduct;
				getProductManagedBean().setProduct(product);
				page = "edit_product";
			} else if (foundProducts.size() == 0) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Nenhum produto encontrado"));
				page = "search_product";
			}
		} catch (NoResultException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(
							"Produto não encontrado com código de barra "
									+ barcode));
			page = "search_product";
		}

		return page;
	}

	public String editProduct() {
		logger.debug("Editing product");
		Long productId = Long
				.parseLong(FacesContext.getCurrentInstance()
						.getExternalContext().getRequestParameterMap()
						.get("productId"));
		logger.debug("Id: " + productId);
		productManagedBean.setProduct((new ProductEntityManager())
				.findById(productId));
		productManagedBean.setProduct((new ProductEntityManager())
				.loadAll(productManagedBean.getProduct()));
		return "edit_product";
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

	public ProductManagedBean getProductManagedBean() {
		return productManagedBean;
	}

	public void setProductManagedBean(ProductManagedBean productManagedBean) {
		this.productManagedBean = productManagedBean;
	}

	private String barcode;
	private String description;
	private List<Product> foundProducts;
	private Product foundProduct;

	@ManagedProperty(value = "#{productManagedBean}")
	private ProductManagedBean productManagedBean;

	private static final long serialVersionUID = 1619203277603284655L;
	private static Logger logger = Logger
			.getLogger(ProductSearchManagedBean.class);
}
