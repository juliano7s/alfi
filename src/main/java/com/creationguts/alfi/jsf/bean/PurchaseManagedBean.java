package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.vo.Purchase;
import com.creationguts.alfi.jsf.bean.util.PurchasedProduct;

@ManagedBean
@SessionScoped
public class PurchaseManagedBean implements Serializable {
	
	@PostConstruct
	public void init() {
		purchasedProducts = new ArrayList<PurchasedProduct>();
		purchasedProducts.add(new PurchasedProduct());
		purchase = new Purchase();
	}
	
	public String addPurchasedProduct() {
		purchasedProducts.add(new PurchasedProduct());
		logger.debug("Added product to the purchase. Now with: " + purchasedProducts.size());
		return "client";
	}
	
	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public List<PurchasedProduct> getPurchasedProducts() {
		return purchasedProducts;
	}

	public void setPurchasedProducts(List<PurchasedProduct> purchasedProducts) {
		this.purchasedProducts = purchasedProducts;
	}

	private List<PurchasedProduct> purchasedProducts;
	private Purchase purchase;
	
	private static Logger logger = Logger.getLogger(PurchaseManagedBean.class);
	private static final long serialVersionUID = 8465332615340553012L;
}
