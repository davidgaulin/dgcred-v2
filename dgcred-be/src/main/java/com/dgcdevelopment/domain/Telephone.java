package com.dgcdevelopment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.hateoas.ResourceSupport;

@Entity
public class Telephone extends ResourceSupport  {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long eid;

	private String countryCode = "";
	private String areaCode = "";
	private String number = "";
	private String international = "";
	
	// to facilitate searches
	private String str;
	
	public String getStr() {
		return str;
	}
	private void updateStr() {
		this.str = countryCode.replaceAll("[\\W]|_", "")
				+ areaCode.replaceAll("[\\W]|_", "")
				+ number.replaceAll("[\\W]|_", "");
	}
	private PhoneType type;

	public Long getEid() {
		return eid;
	}
	public void setEid(Long eid) {
		this.eid = eid;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
		updateStr();
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
		updateStr();
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
		updateStr();
	}
	public String getInternational() {
		return international;
	}
	public void setInternational(String international) {
		this.international = international;
	}
	public PhoneType getType() {
		return type;
	}
	public void setType(PhoneType type) {
		this.type = type;
	}
	public String toString() {
		return this.str;
	}
}
