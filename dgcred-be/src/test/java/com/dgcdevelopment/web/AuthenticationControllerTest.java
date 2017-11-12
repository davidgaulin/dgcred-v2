package com.dgcdevelopment.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.dgcdevelopment.domain.LoginResponse;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.UserRepository;
import com.dgcdevelopment.domain.UserRoleRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserRoleRepository userRoleRepo;
	
	
	@Autowired
	private TestRestTemplate trt;
	
	private User u;
	
	@Before
	@Transactional
	public void setup() throws Exception {
		this.u = dummyUser();
		try {
			//this.userRepo.delete(this.u);
		} catch (Exception e) {
			// ignore
		}
		//this.userRepo.save(this.u);
	}
	
	public User dummyUser() {
		User aUser = new User();
		aUser.setFullname("test test " + Math.random());
		aUser.setUsername("test" + Math.random() + "@test.com");
		aUser.setPassword("test");
		return aUser;
	}
	
	@Test()
	@Transactional
	public void testRegisterUser() throws Exception {
		
		// delete User
		userRepo.deleteByUsername(this.u.getUsername());
		
		// register one
		ResponseEntity<LoginResponse> lr = this.trt.postForEntity("/register", this.u, LoginResponse.class);
		
		// Ensure Password returned is null
		assertNull(lr.getBody().getUser().getPassword());
		
		// Ensure you can login with it once registered
		lr = this.trt.postForEntity("/login", this.u, LoginResponse.class);
		assertNotNull(lr.getBody().getToken());
	}
	
	
	@Test()
	@Transactional
	public void testRegisterMultipleUser() throws Exception {
		userRepo.deleteByUsername(this.u.getUsername());
		userRoleRepo.findAll();
		this.trt.postForEntity("/register", this.u, User.class);
		
		// Ensure you can login with it once registered
		HttpEntity<LoginResponse> lr = this.trt.postForEntity("/login", this.u, LoginResponse.class);
		assertNotNull(lr.getBody().getToken());
				
		this.u.setUsername("b@b.com");
		userRepo.deleteByUsername(this.u.getUsername());
		this.trt.postForEntity("/register", this.u, User.class);
		
		// Ensure you can login with it once registered
		lr = this.trt.postForEntity("/login", this.u, LoginResponse.class);
		assertNotNull(lr.getBody().getToken());

		this.u.setUsername("c@c.com");
		this.trt.postForEntity("/register", this.u, User.class);
		
		// Ensure you can login with it once registered
		lr = this.trt.postForEntity("/login", this.u, LoginResponse.class);
		assertNotNull(lr.getBody().getToken());

	}
	
	
	@Test
	@Transactional
	public void testRegisterInvalidUser() throws Exception {
		User invalidUNoEmail = dummyUser();
		invalidUNoEmail.setUsername(null);
		ResponseEntity<String> aRegU;
		
		aRegU = this.trt.postForEntity("/register", invalidUNoEmail, String.class);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, aRegU.getStatusCode().value());
		assertThat(aRegU.getBody().toLowerCase().contains("required") && aRegU.getBody().toLowerCase().contains("username"));
		
		invalidUNoEmail.setUsername("a@a.com");
		invalidUNoEmail.setPassword(null);
		aRegU = this.trt.postForEntity("/register", invalidUNoEmail, String.class);
		assertEquals(aRegU.getStatusCode().value(), HttpServletResponse.SC_BAD_REQUEST);
		assertThat(aRegU.getBody().toLowerCase().contains("required") && aRegU.getBody().toLowerCase().contains("password"));
		
		invalidUNoEmail.setPassword("a");
		invalidUNoEmail.setFullname("");
		aRegU = this.trt.postForEntity("/register", invalidUNoEmail, String.class);
		assertEquals(aRegU.getStatusCode().value(), HttpServletResponse.SC_BAD_REQUEST);
		assertThat(aRegU.getBody().toLowerCase().contains("required") && aRegU.getBody().toLowerCase().contains("full"));

		invalidUNoEmail.setUsername("a");
		invalidUNoEmail.setFullname("a");
		aRegU = this.trt.postForEntity("/register", invalidUNoEmail, String.class);
		assertEquals(aRegU.getStatusCode().value(), HttpServletResponse.SC_BAD_REQUEST);
		assertThat(aRegU.getBody().toLowerCase().contains("valid") && aRegU.getBody().toLowerCase().contains("email"));

		// Register once
		aRegU = this.trt.postForEntity("/register", this.u, String.class);
		
		// Register again and verify that it fails
		aRegU = this.trt.postForEntity("/register", this.u, String.class);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, aRegU.getStatusCode().value());
		assertThat(aRegU.getBody().toLowerCase().contains("already") && aRegU.getBody().toLowerCase().contains("exist"));
		
	}	
	
	@Test
    public void testInvalidLogin() throws Exception {
		User invalidu = dummyUser();
		invalidu.setPassword("asdfasdf");
		ResponseEntity<LoginResponse> lr = this.trt.postForEntity("/login", invalidu, LoginResponse.class);
		assertEquals(HttpServletResponse.SC_UNAUTHORIZED, lr.getStatusCode().value());
    }
}
