package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.creationguts.alfi.jpa.vo.Purchase;

@ManagedBean
@RequestScoped
public class PurchaseManagedBean implements Serializable {
	
	@PostConstruct
	public void init() {
		purchasedProducts = new HashMap<String, Integer>();
		purchasedProducts.put("", 1);
		purchase = new Purchase();
	}
	
	public String addPurchasedProduct() {
		purchasedProducts.put("", 1);
		return "client";
	}
	
	public Map<String, Integer> getPurchasedProducts() {
		return purchasedProducts;
	}

	public void setPurchasedProducts(Map<String, Integer> purchasedProducts) {
		this.purchasedProducts = purchasedProducts;
	}
	
	public Set<Map.Entry<String,Integer>> getPurchasedProductsEntrySet() {
		return purchasedProducts.entrySet();
	}
	
	public void setPurchasedProductsEntrySet(Set<Map.Entry<String, Integer>> entrySet) {
		purchasedProducts = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : entrySet) {
			purchasedProducts.put(entry.getKey(), entry.getValue());
		}
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	private Map<String, Integer> purchasedProducts;
	private Purchase purchase;
	
	private static final long serialVersionUID = 8465332615340553012L;
}
