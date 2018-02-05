package com.dgcdevelopment.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
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
import com.dgcdevelopment.domain.PhoneType;
import com.dgcdevelopment.domain.Telephone;
import com.dgcdevelopment.domain.lease.Lease;
import com.dgcdevelopment.domain.lease.LeaseRepository;
import com.dgcdevelopment.domain.lease.Tenant;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TenantControllerTest extends AuthenticatedControllerWrapperTest {

	@Autowired
	LeaseRepository leaseRepo;

	private Tenant createOneTenant(String firstName, String lastName) throws Exception {
		Tenant t = new Tenant();
		t.setBirthday(new Date());
		t.setEmail("a@a.com");
		t.setFirstName(firstName);
		t.setLastName(lastName);
		Telephone p = new Telephone();
		p.setAreaCode("819");
		p.setCountryCode("1");
		p.setNumber("639-0999");
		p.setType(PhoneType.MOBILE);
		t.getTelephones().put(p.getType(), p);

		ResponseEntity<Document> doc1 = uploadTextDocument();
		ResponseEntity<Document> doc2 = uploadPdf();
		ResponseEntity<Document> doc3 = uploadPicture();

		Set<Document> docs = new HashSet<>();
		docs.add(doc1.getBody());
		docs.add(doc2.getBody());
		docs.add(doc3.getBody());
		t.setDocuments(docs);

		return t;
	}

	@Test
	public void testDeleteTenant() throws Exception {

		Tenant t = createOneTenant("Peter", "Stashny");

		// Create doc
		ResponseEntity<Tenant> postResult = this.trt.postForEntity("/api/tenant", t, Tenant.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());

		// Check that we can retrive it
		ResponseEntity<Tenant> getResult = this.trt.getForEntity("/api/tenant/" + postResult.getBody().getEid(),
				Tenant.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());

		this.trt.delete("/api/tenant/" + postResult.getBody().getEid());

		// Check that we can't retrive it anymore
		ResponseEntity<Tenant> getNotFoundResult = this.trt.getForEntity("/api/tenant/" + postResult.getBody().getEid(),
				Tenant.class);
		assertTrue(getNotFoundResult.getStatusCode().is4xxClientError());

	}

	@Test
	public void testDeleteTenantWithLease() throws Exception {

		Tenant t = createOneTenant("Peter", "Stashny");

		// Create doc
		ResponseEntity<Tenant> postResult = this.trt.postForEntity("/api/tenant", t, Tenant.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());

		// Check that we can retrive it
		ResponseEntity<Tenant> getResult = this.trt.getForEntity("/api/tenant/" + postResult.getBody().getEid(),
				Tenant.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());

		// Create lease
		Lease l = new Lease();
		l.addTenant(postResult.getBody());
		l.setUser(this.u);
		leaseRepo.save(l);

		// try to delete it
		this.trt.delete("/api/tenant/" + postResult.getBody().getEid());

		// Check that we can still retrive it since the delete failed
		getResult = this.trt.getForEntity("/api/tenant/" + postResult.getBody().getEid(), Tenant.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());

		// Delete tenant from the lease
		l.setTenants(null);
		leaseRepo.save(l);

		// try to delete it again
		this.trt.delete("/api/tenant/" + postResult.getBody().getEid());

		// Check that we can't retrive it anymore
		ResponseEntity<Tenant> getNotFoundResult = this.trt.getForEntity("/api/tenant/" + postResult.getBody().getEid(),
				Tenant.class);
		assertTrue(getNotFoundResult.getStatusCode().is4xxClientError());

	}

	@Test
	public void testTenantCreation() throws Exception {

		Tenant t = createOneTenant("Peter", "Stashny");

		// Create doc
		ResponseEntity<Tenant> postResult = this.trt.postForEntity("/api/tenant", t, Tenant.class);
		assertTrue(postResult.getStatusCode().is2xxSuccessful());

		// Check that we can retrive it
		ResponseEntity<Tenant> getResult = this.trt.getForEntity("/api/tenant/" + postResult.getBody().getEid(),
				Tenant.class);
		assertTrue(getResult.getStatusCode().is2xxSuccessful());

		// Check the data is in there
		assertTrue(getResult.getBody().getDocuments() != null);
		assertTrue(getResult.getBody().getDocuments().size() == 3);
		assertEquals("Peter", getResult.getBody().getFirstName());
		assertEquals("Stashny", getResult.getBody().getLastName());
		assertTrue(downloadTextDocument(((Document) t.getDocuments().toArray()[0]).getEid()));

	}

	@Test
	public void testTenantSearch() throws Exception {

		Tenant t1 = createOneTenant("David", "Gaulin");
		Tenant t2 = createOneTenant("Éric", "Gaulin");
		Tenant t3 = createOneTenant("Gilles", "Carle");
		Tenant t4 = createOneTenant("Sophia", "Laurain");
		Tenant t5 = createOneTenant("Elizabeth", "Sue");

		// save them
		this.trt.postForEntity("/api/tenant", t1, Tenant.class);
		this.trt.postForEntity("/api/tenant", t2, Tenant.class);
		this.trt.postForEntity("/api/tenant", t3, Tenant.class);
		this.trt.postForEntity("/api/tenant", t4, Tenant.class);
		this.trt.postForEntity("/api/tenant", t5, Tenant.class);

		// Search for them
		ResponseEntity<Tenant[]> getResult;
		getResult = this.trt.getForEntity("/api/tenant/search/gaulin", Tenant[].class);
		assertEquals(2, getResult.getBody().length);
		assertTrue(getResult.getBody()[0].getFirstName().equals("David")
				|| getResult.getBody()[1].getFirstName().equals("David"));

		getResult = this.trt.getForEntity("/api/tenant/search/GILLES", Tenant[].class);
		assertEquals(1, getResult.getBody().length);
		assertTrue(getResult.getBody()[0].getFirstName().equals("Gilles"));

		getResult = this.trt.getForEntity("/api/tenant/search/GILLES", Tenant[].class);
		assertEquals(1, getResult.getBody().length);
		assertTrue(getResult.getBody()[0].getFirstName().equals("Gilles"));

		getResult = this.trt.getForEntity("/api/tenant/search/6390999", Tenant[].class);
		assertTrue(getResult.getBody().length >= 5);

	}
}
