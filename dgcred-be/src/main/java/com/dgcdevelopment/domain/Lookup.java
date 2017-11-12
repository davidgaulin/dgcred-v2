package com.dgcdevelopment.domain;

public class Lookup {
	
	private String code;
	private String value;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public Lookup(String code, String value) {
		this.code = code;
		this.value = value;
	}

}
