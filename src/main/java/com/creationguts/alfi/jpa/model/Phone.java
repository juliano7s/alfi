package com.creationguts.alfi.jpa.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Phone implements Serializable {
	
	public Phone() {
		type = "Celular";
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	private String type;
	private String number;
	
	private static final long serialVersionUID = 3730848055972641941L;
}
