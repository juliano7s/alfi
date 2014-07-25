package com.creationguts.alfi.jpa.vo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "clients")
public class Client implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCpf() {
		return this.cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@OneToMany(targetEntity=Order.class, mappedBy="client")
	public List<Order> getOrders() {
		return this.orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="client_phones", joinColumns=@JoinColumn(name="clientid"))
	public List<Phone> getPhoneNumbers() {
		return this.phoneNumbers;
	}
	public void setPhoneNumbers(List<Phone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	
	@Override
	public String toString() {
		return "Client [id=" + this.id + ", name=" + this.name + ", cpf=" + this.cpf
				+ ", address=" + this.address + ", phone=" + this.phone + ", email="
				+ this.email + ", orders=" + this.orders + ", phoneNumbers="
				+ this.phoneNumbers + "]";
	}

	private Long id;
	private String name;
	private String cpf;
	private String address;
	private String phone;
	private String email;
	private List<Order> orders;
	private List<Phone> phoneNumbers;
	
	private static final long serialVersionUID = 3811526042595751057L;
}
