package com.dgcdevelopment.web;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.LoginResponse;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.UserRepository;

public class AuthenticatedControllerWrapperTest {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	protected TestRestTemplate trt;
	
	public User u;
	
	private static boolean authSetupDone = false;
	
	public User dummyUser() {
		User aUser = new User();
		aUser.setFullname("test test");
		aUser.setUsername("test" + Math.random() + "@test.com");
		aUser.setPassword("test");
		HashMap<String, String> preferences = new HashMap<>();
		preferences.put("pref", "1");
		aUser.setPreferences(preferences);
		return aUser;
	}
	
	
	
	@Before
	public void setup() throws Exception {
		if (!authSetupDone || this.u == null) {
			//this.userRepo.deleteAll();
			this.u = dummyUser();
			this.userRepo.save(this.u);
			setAuthInterceptor();
			authSetupDone = true;
		}
	}
	
	private void setAuthInterceptor() {
		ResponseEntity<LoginResponse> lr = this.trt.postForEntity("/login", this.u, LoginResponse.class);
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>(); 
		HeaderRequestInterceptor hri = new HeaderRequestInterceptor("Authorization", "Bearer " + lr.getBody().getToken());
		//HeaderRequestInterceptor hri2 = new HeaderRequestInterceptor("Content-type", MediaType.APPLICATION_JSON.toString());
		System.out.println("Token:" + lr.getBody().getToken());
		interceptors.add(hri);
		//interceptors.add(hri2);
		this.trt.getRestTemplate().setInterceptors(interceptors);
	}
	
	protected ResponseEntity<Document> uploadTextDocument() throws Exception {
		return this.uploadDocument(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
	}
	protected ResponseEntity<Document> uploadPicture() throws Exception {
		return this.uploadDocument(this.getClass().getClassLoader().getResourceAsStream("testpicture.png"));
	}
	protected ResponseEntity<Document> uploadPdf() throws Exception {
		return this.uploadDocument(this.getClass().getClassLoader().getResourceAsStream("testpdf.pdf"));
	}	
	
	protected ResponseEntity<Document> uploadDocument(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuilder fileData = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			fileData.append(line);
		}
				
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new ClassPathResource("application.properties"));
		// Post
		ResponseEntity<Document> result =  this.trt.postForEntity("/api/document", 
				parts, Document.class);
		return result;

	}
	
	protected boolean downloadTextDocument(long id) {
		ResponseEntity<byte[]> result =  this.trt.getForEntity("/api/document/" + id, byte[].class);
		String fc = new String(result.getBody());
		assertTrue(result.getStatusCode().is2xxSuccessful());
		assertTrue(fc.contains("spring"));
		return true;
	}
	protected boolean downloadPicture(long id) {
		ResponseEntity<byte[]> result =  this.trt.getForEntity("/api/document/" + id, byte[].class);
		assertTrue(result.getStatusCode().is2xxSuccessful());
		return true;
	}
	protected boolean downloadPdf(long id) {
		ResponseEntity<byte[]> result =  this.trt.getForEntity("/api/document/" + id, byte[].class);
		assertTrue(result.getStatusCode().is2xxSuccessful());
		return true;
	}	
}
