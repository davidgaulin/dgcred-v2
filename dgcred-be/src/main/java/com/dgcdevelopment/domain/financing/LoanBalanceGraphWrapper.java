package com.dgcdevelopment.domain.financing;

import java.util.ArrayList;
import java.util.List;

import com.dgcdevelopment.domain.Point;

public class LoanBalanceGraphWrapper {
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<Point> getValues() {
		return values;
	}
	public void setValues(List<Point> values) {
		this.values = values;
	}
	private String key;
	private List<Point> values = new ArrayList<>();

}
