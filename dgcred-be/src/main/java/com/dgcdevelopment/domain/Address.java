package com.dgcdevelopment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long eid;
	
	private String address1;
	private String address2;
	
	private String city;
	private String provinceState;
	private String country;
	private String postalZipCode;
	
	public Address() {		
	}
	
	public Address(String address1, String address2, String city, String province, String country, String postalZipCode) {
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.provinceState = province;
		this.country = country;
		this.postalZipCode = postalZipCode;
	}
	
	public String getPostalZipCode() {
		return postalZipCode;
	}
	public void setPostalZipCode(String postalZipCode) {
		this.postalZipCode = postalZipCode;
	}
	public Long getEid() {
		return eid;
	}
	public void setEid(Long eid) {
		this.eid = eid;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvinceState() {
		return provinceState;
	}
	public void setProvinceState(String provinceState) {
		this.provinceState = provinceState;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
