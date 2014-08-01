package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.manager.ProductEntityManager;
import com.creationguts.alfi.jpa.manager.PurchaseEntityManager;
import com.creationguts.alfi.jpa.vo.Product;
import com.creationguts.alfi.jpa.vo.Purchase;
import com.creationguts.alfi.jpa.vo.PurchaseProduct;

@ManagedBean
@SessionScoped
public class PurchaseManagedBean implements Serializable {

	@PostConstruct
	public void init() {
		purchasedProducts = new HashSet<PurchaseProduct>();
		purchase = new Purchase();
	}

	public String savePurchase() {

		PurchaseEntityManager purchaseEntityManager = new PurchaseEntityManager();
		ProductEntityManager productEntityManager = new ProductEntityManager();

		purchase = purchaseEntityManager.save(purchase);

		FacesContext.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage("Compra registrada com Id "
								+ purchase.getId()));

		return "client";
	}

	public String addProduct() {
		if (barcodeToAdd.trim().equals("")) {
			Product p = (new ProductEntityManager()).findByBarcode(barcodeToAdd);
			PurchaseProduct pp = new PurchaseProduct();
			pp.setProduct(p);
			pp.setPurchase(purchase);
			purchasedProducts.add(pp);
		}

		barcodeToAdd = "";
		logger.debug("Added product to the purchase. Now with: "
				+ purchasedProducts.size());
		return "client";
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public String getBarcodeToAdd() {
		return barcodeToAdd;
	}

	public void setBarcodeToAdd(String barcodeToAdd) {
		this.barcodeToAdd = barcodeToAdd;
	}

	public Set<PurchaseProduct> getPurchasedProducts() {
		return purchasedProducts;
	}

	public void setPurchasedProducts(Set<PurchaseProduct> purchasedProducts) {
		this.purchasedProducts = purchasedProducts;
	}

	private Purchase purchase;
	private String barcodeToAdd;
	private Set<PurchaseProduct> purchasedProducts;

	private static Logger logger = Logger.getLogger(PurchaseManagedBean.class);
	private static final long serialVersionUID = 8465332615340553012L;
}
