package com.dgcdevelopment.domain.financing;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.joda.time.DateTime;
import org.joda.time.Months;

import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.property.Property;

@Entity
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eid;

	@ManyToOne
	private FinancialInstitution financialInstitution;

	private String paymentFrequency;

	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public String getCompounding() {
		return compounding;
	}

	public void setCompounding(String compounding) {
		this.compounding = compounding;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	private String compounding;

	/** Full amount of the loan */
	private double amount;

	private Date loanCreationDate;

	public Date getLoanCreationDate() {
		return loanCreationDate;
	}

	public void setLoanCreationDate(Date loanCreationDate) {
		this.loanCreationDate = loanCreationDate;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	private double interestRate;

	private double amortization;

	private double balance;

	private Date balanceDate;

	@ManyToOne
	private Property property;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private User user;

	@OneToMany(cascade = CascadeType.DETACH)
	private Set<Document> documents = new HashSet<>();

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Long getEid() {
		return eid;
	}

	public void setEid(Long eid) {
		this.eid = eid;
	}

	public FinancialInstitution getFinancialInstitution() {
		return financialInstitution;
	}

	public void setFinancialInstitution(FinancialInstitution financialInstitution) {
		this.financialInstitution = financialInstitution;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmortization() {
		return amortization;
	}

	public void setAmortization(double amortization) {
		this.amortization = amortization;
	}

	@PreRemove
	private void removePropertyAndFinancialInstitution() {
		property = null;
		// financialInstitution = null;
	}

	public double calculateBalanceAt(Date date) {
		double mir = this.interestRate / 12 / 100;
		double mp = getCalculatedMonthlyPaiement();
		int nomslp = getNumOfMonthSinceLastBalance(date);
		double remainCapital = 0;
		if (this.balanceDate != null) {
			remainCapital = this.balance;
		} else {
			remainCapital = this.amount;
		}
		// loan should be decreasing
		if (nomslp > 0) {
			for (int x = 0; x < nomslp; x++) {
				remainCapital = remainCapital - (mp - (remainCapital * mir));
			}

			// Going back in the past, amount should increase
		} else {
			for (int x = nomslp; x < 0 && remainCapital < this.amount; x++) {
				remainCapital = remainCapital + (mp - (remainCapital * mir));
			}
		}
		if (remainCapital > 0) {
			return Math.round(remainCapital * 100) / 100D;
		} else {
			return 0;
		}
	}

	public double calculateCurrentBalance() {
		return calculateBalanceAt(new Date());
	}

	private int getNumOfMonthSinceLastBalance(Date aDate) {
		if (this.balanceDate != null) {
			return Months.monthsBetween(new DateTime(this.balanceDate.getTime()), new DateTime(aDate.getTime()))
					.getMonths();
		} else {
			return Months.monthsBetween(new DateTime(this.loanCreationDate.getTime()), new DateTime(aDate.getTime()))
					.getMonths();
		}
	}

	public double getCalculatedMonthlyPaiement() {
		double mir = this.interestRate / 12 / 100;
		// Terms in Month
		double tim = this.amortization * 12;
		double mp = (amount * mir) / (1 - Math.pow(1 + mir, -tim));
		// double mp = a / b;
		return mp;
	}

	private Date renewalDate;
	private int termsInMonth;

	public Date getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	public int getTermsInMonth() {
		return termsInMonth;
	}

	public void setTermsInMonth(int termsInMonth) {
		this.termsInMonth = termsInMonth;
	}

}
