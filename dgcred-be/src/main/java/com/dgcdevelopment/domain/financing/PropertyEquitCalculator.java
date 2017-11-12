package com.dgcdevelopment.domain.financing;

import java.util.HashSet;
import java.util.Set;

import com.dgcdevelopment.domain.property.Property;

public class PropertyEquitCalculator {
	
	private Property property;
	
	private Set<Loan> loans = new HashSet<>();
	
	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Set<Loan> getLoans() {
		return loans;
	}

	public void setLoans(Set<Loan> loans) {
		this.loans = loans;
	}

	public double getPrincipal() {
		return 1;
	}
	
	public double getEquity() {
		return 1;
	}
	
	public double getAvailablePrincipal() {
		return 1;
	}
}
