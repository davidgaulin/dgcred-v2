package com.dgcdevelopment.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address extends BaseEntity {

	private String address1;
	private String address2;

	private String city;
	private String provinceState;
	private String country;
	private String postalZipCode;

	public Address() {
	}

	public Address(String address1, String address2, String city, String province, String country,
			String postalZipCode) {
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.provinceState = province;
		this.country = country;
		this.postalZipCode = postalZipCode;
	}

}
