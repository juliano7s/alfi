package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.manager.ClientEntityManager;
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
		logger.debug("Initializing purchaseManagedBean");
		purchasedProducts = new HashSet<PurchaseProduct>();
		purchase = new Purchase();
	}

	public String editPurchase() {
		logger.debug("Editing purchase");
		Long purchaseId = Long.parseLong(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap()
				.get("purchaseId"));
		logger.debug("Id: " + purchaseId);
		String fromPage = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("fromPage");
		logger.debug("fromPage: " + fromPage);

		FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.put("fromPage", fromPage);
		purchase = (new PurchaseEntityManager()).findById(purchaseId);
		purchase = new PurchaseEntityManager().loadAll(purchase);
		purchasedProducts = purchase.getPurchaseProducts();
		for (PurchaseProduct purchaseProduct : purchasedProducts) {
			logger.debug("pp: " + purchaseProduct.getProduct().getBarcode());
		}

		return "edit_purchase";
	}

	public String savePurchase() {

		String clientIdParam = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("clientId");
		logger.debug("Adding purchase to client with id " + clientIdParam);
		Long clientId = clientIdParam != null ? Long.parseLong(clientIdParam)
				: 0L;
		purchase.setClient((new ClientEntityManager()).findById(clientId));
		purchase.setPurchaseProducts(purchasedProducts);
		PurchaseEntityManager purchaseEntityManager = new PurchaseEntityManager();
		purchase = purchaseEntityManager.save(purchase);

		FacesContext.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage("Compra registrada com Id "
								+ purchase.getId()));

		return "client";
	}

	public String addProduct() {
		String fromPage = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("fromPage");
		logger.debug("fromPage: " + fromPage);

		try {
		if (!barcodeToAdd.trim().equals("")) {
			Product p = (new ProductEntityManager())
					.findByBarcode(barcodeToAdd);
			PurchaseProduct pp = new PurchaseProduct();
			pp.setProduct(p);
			pp.setPurchase(purchase);
			purchasedProducts.add(pp);
			logger.debug("Added product to the purchase. Now with: "
					+ purchasedProducts.size());
		} else {
			logger.debug("barcode empty. Product not added to purchase.");
		}
		} catch (NoResultException e) {
			logger.warn("Product with barcode " + barcodeToAdd + " not found");
			FacesContext.getCurrentInstance()
			.addMessage(
					null,
					new FacesMessage("Produto com código " + barcodeToAdd + " não encontrado"));
		}

		barcodeToAdd = "";

		return fromPage;
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

	public List<PurchaseProduct> getPurchasedProductsList() {
		return new ArrayList<PurchaseProduct>(purchasedProducts);
	}

	public void setPurchasedProductsList(List<PurchaseProduct> productList) {
		purchasedProducts = new HashSet<PurchaseProduct>(productList);
	}

	private Purchase purchase;
	private String barcodeToAdd;
	private Set<PurchaseProduct> purchasedProducts;

	private static Logger logger = Logger.getLogger(PurchaseManagedBean.class);
	private static final long serialVersionUID = 8465332615340553012L;
}
