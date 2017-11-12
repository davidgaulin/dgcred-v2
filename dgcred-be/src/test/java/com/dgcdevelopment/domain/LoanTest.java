package com.dgcdevelopment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.dgcdevelopment.domain.financing.Loan;

public class LoanTest {

	Loan l; 
	
	@Before
	public void setup() throws Exception {
		l = new Loan();
		Date creationDate = DateTime.now().minusMonths(6).toDate();
		l.setAmount(100);
		l.setBalance(100);
		l.setBalanceDate(creationDate);
		l.setLoanCreationDate(creationDate);
		l.setInterestRate(10);
		l.setAmortization(10);
		
	}
	
	@Test
	public void testMonthlyPaiement() {
		double mp = l.getCalculatedMonthlyPaiement();
		double mpRounded = Math.round(mp * 100) / 100D;
		System.out.println("Monthly paiement: " + mp);
		assertThat(mpRounded == 1.32);
	}
	
	@Test
	public void testCurrentBalance() {
		double balance = l.calculateCurrentBalance();
		System.out.println("Balance after 6 months: " + balance);
		assertThat(balance == 97.01);
	}
	
	@Test
	public void testFinalPayOff() {
		l.setLoanCreationDate(DateTime.now().minusYears(10).toDate());
		l.setBalanceDate(l.getLoanCreationDate());
		l.setBalance(l.getAmount());
		double balance = l.calculateCurrentBalance();
		System.out.println("Balance after 10 years: " + balance);
		assertThat(balance == 0);
	}
	
	@Test
	public void testBalanceInPast() {
		l.setLoanCreationDate(DateTime.now().minusYears(10).toDate());
		l.setBalanceDate(l.getLoanCreationDate());
		l.setBalance(l.getAmount());
		double balance = l.calculateCurrentBalance();
		System.out.println("Balance after 10 years: " + balance);
		assertThat(balance == 0);
	}
	
	@Test
	public void testNotAllPaid() {
		l.setLoanCreationDate(DateTime.now().minusYears(9).minusMonths(11).toDate());
		l.setBalanceDate(l.getLoanCreationDate());
		l.setBalance(l.getAmount());
		double balance = l.calculateCurrentBalance();
		System.out.println("Balance after 9 years 11 months: " + balance);
		assertThat(balance < l.getCalculatedMonthlyPaiement());
	}
	
	@Test
	public void testCalculatePastBalanced() {
		
		l = new Loan();
		Date creationDate = DateTime.now().minusYears(1).toDate();
		l.setAmount(100);
		l.setBalance(97.01);
		l.setBalanceDate(DateTime.now().minusMonths(6).toDate());
		l.setLoanCreationDate(creationDate);
		l.setInterestRate(10);
		l.setAmortization(10);
		double balance = l.calculateBalanceAt(DateTime.now().minusMonths(9).toDate());
		System.out.println("Balance after 3 months when balance was entered at 6: " + balance);		
		assertThat(98.52 == balance);
	}
}
