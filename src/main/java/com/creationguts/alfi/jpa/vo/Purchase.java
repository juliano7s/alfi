package com.creationguts.alfi.jpa.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "purchases")
public class Purchase implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="clientid", nullable=false)
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(name="payment_date")
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	@Column(name="paid_status")
	public Float getPaidValue() {
		return paidValue;
	}
	public void setPaidValue(Float paidValue) {
		this.paidValue = paidValue;
	}
	@Column(name="paid_status")
	public Boolean getPaidStatus() {
		return paidStatus;
	}
	public void setPaidStatus(Boolean paidStatus) {
		this.paidStatus = paidStatus;
	}
	
	@ManyToMany(targetEntity = Product.class, fetch = FetchType.LAZY)
	@JoinTable(name = "purchase_product_xref", joinColumns = { @JoinColumn(name = "purchaseid", referencedColumnName = "id") })
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	private Long id;
	private Client client;
	private Date date;
	private Date paymentDate;
	private Float paidValue;
	private Boolean paidStatus;
	private Set<Product> products;
	
	private static final long serialVersionUID = -1899087253078689858L;
}
