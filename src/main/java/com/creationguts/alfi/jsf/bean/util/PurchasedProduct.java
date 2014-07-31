package com.creationguts.alfi.jsf.bean.util;

public class PurchasedProduct {
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	private String barcode;
	private Integer quantity;
}
