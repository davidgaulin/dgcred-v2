package com.dgcdevelopment.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
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

import com.dgcdevelopment.domain.Address;
import com.dgcdevelopment.domain.AreaUnits;
import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.RentPeriod;
import com.dgcdevelopment.domain.financing.FinancialInstitutionRepository;
import com.dgcdevelopment.domain.financing.Loan;
import com.dgcdevelopment.domain.financing.LoanRepository;
import com.dgcdevelopment.domain.lease.Lease;
import com.dgcdevelopment.domain.lease.LeaseRepository;
import com.dgcdevelopment.domain.lease.Tenant;
import com.dgcdevelopment.domain.lease.TenantRepository;
import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.Unit;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PropertyControllerTest extends AuthenticatedControllerWrapperTest {
	
	@Autowired
	private LoanRepository loanRepo;
	
	@Autowired
	private FinancialInstitutionRepository finInstRepo;
	
	@Autowired
	private TenantRepository tenantRepo;
	
	@Autowired 
	private LeaseRepository leaseRepo;
	
	private Property createOneProperty(String name) throws Exception {
		Property p = new Property();
		p.setName(name);
		p.setConstructionYear(1900);
		p.setEvaluation(1234);
		p.setAddress(new Address("1", "2", "3", "4", "5", "6"));
		
		ResponseEntity<Document> doc1 = uploadTextDocument();
		ResponseEntity<Document> doc2 = uploadPdf();
		ResponseEntity<Document> doc3 = uploadPicture();

		Set<Document> docs = new HashSet<>();
		docs.add(doc1.getBody());
		docs.add(doc2.getBody());
		docs.add(doc3.getBody());
		p.setDocuments(docs);
		
		Unit u = new Unit();
		u.setArea(12);
		u.setAreaUnit(AreaUnits.SQUARE_FEET);
		u.setNumber("a");
		u.setProjectedRent(123);
		u.setRentPeriod(RentPeriod.MONTHS);
		ResponseEntity<Document> doc4 = uploadPicture();
		u.addPicture(doc4.getBody());
		p.addUnit(u);
		
		return p;
	}
	
	@Test
	public void testPropertyCreation() throws Exception {
		
		Property p = createOneProperty("1");
			
		// Create doc
		ResponseEntity<Property> postResult =  this.trt.postForEntity("/api/property", p, Property.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());

		
		// Check that we can retrive it
		ResponseEntity<Property> getResult =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		
		// Check the data is in there
		assertTrue(getResult.getBody().getDocuments() != null);
		assertTrue(getResult.getBody().getDocuments().size() == 3);
		assertTrue(getResult.getBody().getUnits() != null);
		assertTrue(getResult.getBody().getUnits().size() == 1);
		assertTrue(getResult.getBody().getUnits().toArray(new Unit[1])[0].getNumber().equals("a"));
		assertTrue(downloadTextDocument(((Document) p.getDocuments().toArray()[0]).getEid()));
		assertTrue(downloadPicture(
				((Unit) p.getUnits().toArray()[0]).getPictures().get(0).getEid()));
		
	}


	@Test
	public void testMultiplePropertyCreation() throws Exception {
		
		Property p = createOneProperty("1");
			
		// Create doc
		ResponseEntity<Property> postResult =  this.trt.postForEntity("/api/property", p, Property.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());
		
		// Check that we can retrive it
		ResponseEntity<Property> getResult =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		
		// Check the data is in there
		assertTrue(getResult.getBody().getDocuments() != null);
		assertTrue(getResult.getBody().getDocuments().size() == 3);
		assertTrue(getResult.getBody().getUnits() != null);
		assertTrue(getResult.getBody().getUnits().size() == 1);
		assertTrue(getResult.getBody().getUnits().toArray(new Unit[1])[0].getNumber().equals("a"));
		assertTrue(downloadTextDocument(((Document) p.getDocuments().toArray()[0]).getEid()));
		assertTrue(downloadPicture(
				((Unit) p.getUnits().toArray()[0]).getPictures().get(0).getEid()));

		p = createOneProperty("2");
		
		// Create doc
		postResult =  this.trt.postForEntity("/api/property", p, Property.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());
		
		// Check that we can retrive it
		getResult =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		
		// Check the data is in there
		assertTrue(getResult.getBody().getDocuments() != null);
		assertTrue(getResult.getBody().getDocuments().size() == 3);
		assertTrue(getResult.getBody().getUnits() != null);
		assertTrue(getResult.getBody().getUnits().size() == 1);
		assertTrue(getResult.getBody().getUnits().toArray(new Unit[1])[0].getNumber().equals("a"));
		assertTrue(downloadTextDocument(((Document) p.getDocuments().toArray()[0]).getEid()));
		assertTrue(downloadPicture(
				((Unit) p.getUnits().toArray()[0]).getPictures().get(0).getEid()));

		
	}

	@Test
	public void testDeletePropertyWithLease() throws Exception {
		
		Property p = createOneProperty("2");
			
		// Create doc
		ResponseEntity<Property> postResult =  this.trt.postForEntity("/api/property", p, Property.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());
		
		// Check that we can retrive it
		ResponseEntity<Property> getResult =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());

		// Add a lease
		Lease l = new Lease();
		l.setUnit((Unit) getResult.getBody().getUnits().toArray()[0]);
		l.setUser(this.u);
		Tenant t = new Tenant();
		t.setUser(this.u);
		tenantRepo.save(t);
		leaseRepo.save(l);
		
		// try to delete it
		this.trt.delete("/api/property/" + postResult.getBody().getEid());		
		
		// Check that we can still retrieve it since we had not update the loan
		ResponseEntity<Property> getFound =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getFound.getStatusCode().is2xxSuccessful());
		
		// Remove the tenant and lease
		l.setTenants(null);
		leaseRepo.save(l);
		tenantRepo.delete(t);
		leaseRepo.delete(l);
		
		// try to delete it
		this.trt.delete("/api/property/" + postResult.getBody().getEid());		
				
		// Check that we can still retrieve it since we had not update the loan
		ResponseEntity<Property> getNotFound =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getNotFound.getStatusCode().is4xxClientError());
		
	}
	
	
	@Test
	public void testDeletePropertyWithLoan() throws Exception {
		
		Property p = createOneProperty("2");
			
		// Create doc
		ResponseEntity<Property> postResult =  this.trt.postForEntity("/api/property", p, Property.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());
		
		// Check that we can retrive it
		ResponseEntity<Property> getResult =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());

		// Add a loan
		Loan l = new Loan();
		l.setProperty(getResult.getBody());
		l.setFinancialInstitution(finInstRepo.findOneByEid(new Long(0)));
		l.setUser(this.u);
		l.setAmount(100);
		l.setAmortization(25);
		l.setInterestRate(5);
		loanRepo.save(l);
		
		// try to delete it
		this.trt.delete("/api/property/" + postResult.getBody().getEid());		
		
		// Check that we can still retrieve it since we had not update the loan
		ResponseEntity<Property> getFound =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getFound.getStatusCode().is2xxSuccessful());
		
		l.setProperty(null);
		loanRepo.save(l);
		
		// try to delete it
		this.trt.delete("/api/property/" + postResult.getBody().getEid());		
				
		// Check that we can still retrieve it since we had not update the loan
		ResponseEntity<Property> getNotFound =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getNotFound.getStatusCode().is4xxClientError());
				
	}
	
	
	@Test
	public void testDeleteProperty() throws Exception {
		
		Property p = createOneProperty("2");
			
		// Create doc
		ResponseEntity<Property> postResult =  this.trt.postForEntity("/api/property", p, Property.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());

		
		// Check that we can retrive it
		ResponseEntity<Property> getResult =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());
		
		// Check that we can delete it
		this.trt.delete("/api/property/" + postResult.getBody().getEid());
		
		// Check that we can't retrieve it anymore
		ResponseEntity<Property> getNotFound =  this.trt.getForEntity("/api/property/" + postResult.getBody().getEid(), Property.class);
		assertTrue(getNotFound.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void testMidPoint() {
		Property p1 = new Property();
		p1.setName("P1");
		p1.setConstructionYear(1900);
		p1.setEvaluation(1234);
		p1.setAddress(new Address("419 rue des Navigateurs", "", "Gatineau", "QC", "CA", "J9J2L6"));
		
		Property p2 = new Property();
		p2.setName("P1");
		p2.setConstructionYear(1900);
		p2.setEvaluation(1234);
		p2.setAddress(new Address("252 rue du Louvre", "", "Gatineau", "QC", "CA", "J9X 1HA"));
		
		this.trt.postForEntity("/api/property", p1, Property.class);
		this.trt.postForEntity("/api/property", p2, Property.class);
		
		ResponseEntity<HashMap> getResult = null;
		getResult =  this.trt.getForEntity("/api/property/midPoint", HashMap.class);
		assertEquals(9.0, getResult.getBody().get("zoom"));
		
		// TODO
//		getResult =  this.trt.getForEntity("/api/property/midPoint/300" , double[].class);
//		assertEquals(12, getResult.getBody()[2], 0);
	}
	
}
