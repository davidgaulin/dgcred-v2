package com.dgcdevelopment.web;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.financing.FinancialInstitutionRepository;
import com.dgcdevelopment.domain.financing.Loan;
import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.PropertyRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoanControllerTest extends AuthenticatedControllerWrapperTest {
	
	@Autowired
	private FinancialInstitutionRepository fiRepo;
	
	@Autowired
	private PropertyRepository pr;
	
	private Loan createOneLoan(double amount) throws Exception {
		Loan l = new Loan();
		l.setAmount(amount);
		l.setInterestRate(3.5);
		l.setAmortization(25);
		l.setFinancialInstitution(fiRepo.findOneByEid(new Long(0)));
		
		ResponseEntity<Document> doc1 = uploadTextDocument();

		Set<Document> docs = new HashSet<>();
		docs.add(doc1.getBody());
		l.setDocuments(docs);
		
		return l;
	}
	
	@Test
	public void testLoanCreation() throws Exception {
		
		Loan p = createOneLoan(123);
			
		// Create doc
		ResponseEntity<Loan> postResult =  this.trt.postForEntity("/api/loan", p, Loan.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());

		
		// Check that we can retrive it
		ResponseEntity<Loan> getResult =  this.trt.getForEntity("/api/loan/" + postResult.getBody().getEid(), Loan.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		
		// Check the data is in there
		assertTrue(getResult.getBody().getDocuments() != null);
		assertTrue(getResult.getBody().getDocuments().size() == 1);
		assertTrue(downloadTextDocument(((Document) p.getDocuments().toArray()[0]).getEid()));
		
	}
	
	@Test
	public void testDeleteLoan() throws Exception {
		
		Loan p = createOneLoan(1);
			
		// Create doc
		ResponseEntity<Loan> postResult =  this.trt.postForEntity("/api/loan", p, Loan.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());

		
		// Check that we can retrive it
		ResponseEntity<Loan> getResult =  this.trt.getForEntity("/api/loan/" + postResult.getBody().getEid(), Loan.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		
		// Check that we can delete it
		this.trt.delete("/api/loan/" + postResult.getBody().getEid());
		
		// Check that we can't retrieve it anymore
		ResponseEntity<Loan> getNotFound =  this.trt.getForEntity("/api/loan/" + postResult.getBody().getEid(), Loan.class);
		assertTrue(getNotFound.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void testGetLoanForProperty() throws Exception {
		
		Loan l = createOneLoan(1);
		Property p = new Property();
		p.setUser(this.u);
		p.setName("ASDf");
		pr.save(p);
		l.setProperty(p);

		// Create doc
		ResponseEntity<Loan> postResult =  this.trt.postForEntity("/api/loan", l, Loan.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());

		
		// Check that we can retrive it
		ResponseEntity<Loan> getResult =  this.trt.getForEntity("/api/loan/" + postResult.getBody().getEid(), Loan.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		
		// Check that we can delete it
		this.trt.delete("/api/loan/" + postResult.getBody().getEid());
		
		// Check that we can't retrieve it anymore
		ResponseEntity<Loan> getNotFound =  this.trt.getForEntity("/api/loan/" + postResult.getBody().getEid(), Loan.class);
		assertTrue(getNotFound.getStatusCode().is4xxClientError());
	}	

}
