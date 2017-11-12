package com.dgcdevelopment.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.dgcdevelopment.domain.financing.FinancialInstitutionRepository;
import com.dgcdevelopment.domain.financing.Loan;
import com.dgcdevelopment.domain.financing.LoanBalanceGraphWrapper;
import com.dgcdevelopment.domain.financing.LoanRepository;
import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.PropertyRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FinancialGraphControllerTest extends AuthenticatedControllerWrapperTest {
	
	@Autowired
	private FinancialInstitutionRepository fiRepo;
	
	@Autowired
	private LoanRepository loanRepo;
	
	@Autowired 
	private PropertyRepository propertyRepo;
	
	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	@Test
	public void testTwoLoanGraph6Years3Past3future() throws Exception {
		
		Loan l1 = new Loan();
		l1.setAmount(100);
		l1.setInterestRate(10);
		l1.setAmortization(10);
		l1.setLoanCreationDate(DateTime.now().minusYears(10).toDate());
		l1.setFinancialInstitution(fiRepo.findOneByEid(new Long(0)));
		l1.setUser(this.u);

		Loan l2 = new Loan();
		l2.setAmount(100);
		l2.setInterestRate(10);
		l2.setAmortization(10);
		l2.setLoanCreationDate(DateTime.now().minusYears(8).toDate());
		l2.setFinancialInstitution(fiRepo.findOneByEid(new Long(0)));
		l2.setUser(this.u);
		
		loanRepo.save(l1);
		loanRepo.save(l2);
			
		ResponseEntity<LoanBalanceGraphWrapper[]> getResult =  this.trt.getForEntity("/api/fg/loanbalance/previsions/years/-3/3", LoanBalanceGraphWrapper[].class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		assertThat(getResult.getBody().length == 2);
		assertThat(getResult.getBody()[0].getKey().contains("Desjardins") && getResult.getBody()[0].getKey().contains("100"));
		assertThat(getResult.getBody()[0].getValues().size() == 6);
		assertThat(getResult.getBody()[0].getValues().get(0).getY().equals("29.71"));
		
		System.out.println(getResult.getBody()[0]);
	}

	@Test
	public void testOverallValue10Years5Past5future() throws Exception {
		
		Loan l1 = new Loan();
		l1.setAmount(100);
		l1.setInterestRate(10);
		l1.setAmortization(10);
		l1.setLoanCreationDate(DateTime.now().minusYears(10).toDate());
		l1.setFinancialInstitution(fiRepo.findOneByEid(new Long(0)));
		l1.setUser(this.u);

		Loan l2 = new Loan();
		l2.setAmount(100);
		l2.setInterestRate(10);
		l2.setAmortization(10);
		l2.setLoanCreationDate(DateTime.now().minusYears(8).toDate());
		l2.setFinancialInstitution(fiRepo.findOneByEid(new Long(0)));
		l2.setUser(this.u);
		
		loanRepo.save(l1);
		loanRepo.save(l2);
		
		Property p1 = new Property();
		p1.setPurchaseDate(DateTime.now().minusYears(10).toDate());
		p1.setPurchasePrice(120);
		p1.setYearlyAppreciationPercentage(5);
		p1.setUser(this.u);
		
		Property p2 = new Property();
		p2.setPurchaseDate(DateTime.now().minusYears(8).toDate());
		p2.setPurchasePrice(120);
		p2.setEvaluationDate(DateTime.now().minusYears(4).toDate());
		p2.setEvaluation(150);
		p2.setYearlyAppreciationPercentage(10);
		p2.setUser(this.u);
		
		propertyRepo.save(p1);
		propertyRepo.save(p2);
		
		ResponseEntity<LoanBalanceGraphWrapper[]> getResult =  this.trt.getForEntity("/api/fg/overallvalue/previsions/years/-5/5", LoanBalanceGraphWrapper[].class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		assertEquals(getResult.getBody().length, 2);
		assertTrue(getResult.getBody()[0].getKey().contains("Debt"));
		assertEquals(getResult.getBody()[0].getValues().size(),10);
		// TODO REview numbers before going to prod
		assertEquals(getResult.getBody()[0].getValues().get(0).getX(),"2012");
		assertEquals(getResult.getBody()[0].getValues().get(0).getY(),"126.63");
		assertEquals(getResult.getBody()[0].getValues().get(9).getX(),"2021");
		assertEquals(getResult.getBody()[0].getValues().get(9).getY(),"0.0");
		
		
		assertTrue(getResult.getBody()[1].getKey().contains("Value"));
		
		// TODO REview numbers before going to prod
		assertEquals(getResult.getBody()[1].getValues().get(0).getX(),("2012"));
		assertEquals(getResult.getBody()[1].getValues().get(0).getY(),"309.29");
		assertEquals(getResult.getBody()[1].getValues().get(9).getX(),"2021");
		assertEquals(getResult.getBody()[1].getValues().get(9).getY(), "613.06");
	}
	

	@Test
	public void testOverallValue2Years1Past1future() throws Exception {
		
		Loan l1 = new Loan();
		l1.setAmount(100);
		l1.setInterestRate(10);
		l1.setAmortization(10);
		l1.setLoanCreationDate(new DateTime().minusYears(1).withMonthOfYear(12).withDayOfMonth(31).toDate());
		l1.setFinancialInstitution(fiRepo.findOneByEid(new Long(0)));
		l1.setUser(this.u);

		Loan l2 = new Loan();
		l2.setAmount(100);
		l2.setInterestRate(10);
		l2.setAmortization(10);
		l2.setLoanCreationDate(new DateTime().minusYears(1).withMonthOfYear(12).withDayOfMonth(31).toDate());
		l2.setFinancialInstitution(fiRepo.findOneByEid(new Long(0)));
		l2.setUser(this.u);
		
		loanRepo.save(l1);
		loanRepo.save(l2);
		
		Property p1 = new Property();
		p1.setPurchaseDate(new DateTime().minusYears(1).withMonthOfYear(12).withDayOfMonth(31).toDate());
		p1.setPurchasePrice(120);
		p1.setYearlyAppreciationPercentage(10);
		p1.setUser(this.u);
		
		Property p2 = new Property();
		p2.setPurchaseDate(new DateTime().minusYears(1).withMonthOfYear(12).withDayOfMonth(31).withMonthOfYear(12).toDate());
		p2.setPurchasePrice(120);
		p2.setYearlyAppreciationPercentage(10);
		p2.setUser(this.u);
		
		propertyRepo.save(p1);
		propertyRepo.save(p2);
		
		ResponseEntity<LoanBalanceGraphWrapper[]> getResult =  this.trt.getForEntity("/api/fg/overallvalue/previsions/years/-1/2", LoanBalanceGraphWrapper[].class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		assertTrue(getResult.getBody().length == 2);
		assertTrue(getResult.getBody()[0].getKey().contains("Debt"));
		assertEquals(getResult.getBody()[0].getValues().size(), 3);
		// TODO REview numbers before going to prod
		assertEquals(getResult.getBody()[0].getValues().get(0).getX(),"2016");
		assertEquals(getResult.getBody()[0].getValues().get(0).getY(),"200.0");
		assertEquals(getResult.getBody()[0].getValues().get(2).getX(),"2018");
		assertEquals(getResult.getBody()[0].getValues().get(2).getY(),"174.18");
		
		// TODO REview numbers before going to prod
		assertTrue(getResult.getBody()[1].getKey().contains("Value"));
		assertEquals(getResult.getBody()[0].getValues().size(), 3);
		assertEquals(getResult.getBody()[1].getValues().get(0).getX(),"2016");
		assertEquals(getResult.getBody()[1].getValues().get(0).getY(),"240.0");
		assertEquals(getResult.getBody()[1].getValues().get(2).getX(),"2018");
		assertEquals(getResult.getBody()[1].getValues().get(2).getY(),"292.9");
	}
	
}
