package com.dgcdevelopment.domain;

public enum PhoneType {
	MOBILE("Mobile"), OFFICE("Office"), HOME("Home"), FAX("Fax"), PAGER("Pager"), OTHER("Other"); 
	private String value; 
	private PhoneType(String value) { this.value = value; }
    public String toString() {
        return this.value;
     }
}
