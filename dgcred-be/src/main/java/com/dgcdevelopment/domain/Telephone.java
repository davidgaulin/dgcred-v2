package com.dgcdevelopment.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Telephone extends BaseEntity {

	private String countryCode = "";
	private String areaCode = "";
	private String number = "";
	private String international = "";

	// to facilitate searches
	private String str;

	private void updateStr() {
		this.str = countryCode.replaceAll("[\\W]|_", "") + areaCode.replaceAll("[\\W]|_", "")
				+ number.replaceAll("[\\W]|_", "");
	}

	private PhoneType type;

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
		updateStr();
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
		updateStr();
	}

	public void setNumber(String number) {
		this.number = number;
		updateStr();
	}
}
