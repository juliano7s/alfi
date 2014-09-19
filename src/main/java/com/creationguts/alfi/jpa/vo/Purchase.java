package com.creationguts.alfi.jpa.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@Column(name="paid_value")
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
	
	@OneToMany(targetEntity = PurchaseProduct.class, mappedBy = "purchase", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	public Set<PurchaseProduct> getPurchaseProducts() {
		return purchasedProducts;
	}
	
	public void setPurchaseProducts(Set<PurchaseProduct> products) {
		purchasedProducts = products;
	}
	
	@Override
	public String toString() {
		return "Purchase [id=" + id + ", client=" + client + ", date=" + date
				+ ", paymentDate=" + paymentDate + ", paidValue=" + paidValue
				+ ", paidStatus=" + paidStatus + "]";
	}

	private Long id;
	private Client client;
	private Date date;
	private Date paymentDate;
	private Float paidValue;
	private Boolean paidStatus;
	private Set<PurchaseProduct> purchasedProducts;
	
	private static final long serialVersionUID = -1899087253078689858L;
}
