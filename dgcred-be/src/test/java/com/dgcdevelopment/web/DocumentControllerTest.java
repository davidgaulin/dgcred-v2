package com.dgcdevelopment.web;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentControllerTest extends AuthenticatedControllerWrapperTest {

	
	@Test
	public void testGetDocument() throws Exception {
		long eid = uploadTextDocument().getBody().getEid();
		ResponseEntity<byte[]> result =  this.trt.getForEntity("/api/document/" + eid, byte[].class);
		String fc = new String(result.getBody());
		assertTrue(result.getStatusCode().is2xxSuccessful());
		assertTrue(fc.contains("spring"));
	}
	
	@Test
	public void testGetPdf() throws Exception {
		long id = uploadPdf().getBody().getEid();
		downloadPdf(id);
	}	
	
	@Test
	public void testGetPicture() throws Exception {
		long eid = uploadPicture().getBody().getEid();
		downloadPicture(eid);
	}
}
