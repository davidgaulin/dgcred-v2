package com.dgcdevelopment.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dgcdevelopment.domain.LoginResponse;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.exceptions.InvalidInformationException;
import com.dgcdevelopment.domain.exceptions.MissingInformationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dgcdevelopment.domain.UserRepository;
import com.dgcdevelopment.domain.UserRoleRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@CrossOrigin
public class AuthenticationController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;
	
	@PostMapping("/register")
    public LoginResponse register(@RequestBody String payload, HttpServletResponse response) throws Exception {
		
		log.info("Registration Request received...");
		
		ObjectMapper om = new ObjectMapper();
		User input = om.readValue(payload, User.class);
		input.getRoles().add(userRoleRepo.findOne(new Long(1)));
		
		log.info("Validating user: " + input.getUsername() + "...");
		if (StringUtils.isEmpty(input.getUsername())
				|| StringUtils.isEmpty(input.getPassword())
				|| StringUtils.isEmpty(input.getFullname())
				|| StringUtils.isEmpty(input.getUsername())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			throw new MissingInformationException("Full name, username and password are required");
		}
		
		if (!EmailValidator.getInstance().isValid(input.getUsername())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			throw new InvalidInformationException("Username must be a valid email address");
		}
		
		User user = userRepo.findOneByUsername(input.getUsername());
		if (user != null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			throw new InvalidInformationException("User already exists");
		}
		log.info("Registering user: " + input.getUsername() + ":" + input.getFullname() + "...");
		input = userRepo.save(input);
		
		// Empty the password field to not send it back
		input.setPassword(null);
		
		log.info("Registration Request completed");
		return generateLoginResponse(input);
    }
	
	@PostMapping("/login")
    public LoginResponse login(@RequestBody String payload, HttpServletResponse response) throws Exception {
		
		log.debug("Login Request received...");
		
		ObjectMapper om = new ObjectMapper();
		User input = om.readValue(payload, User.class);
		
		log.info("Login in user:" + input.getUsername());
		
		List<User> users = userRepo.findByUsernameAndPassword(input.getUsername(), input.getPassword());
		
		if (users.size() > 0) {
	        return generateLoginResponse(users.get(0));
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
    }
	
	private LoginResponse generateLoginResponse(User u) {
		return new LoginResponse(
        		Jwts.builder().setSubject(u.getUsername())
        			.claim("roles", u.getRoles())
        				.setIssuedAt(new Date())
        					.signWith(SignatureAlgorithm.HS256, "secretkey").compact(), u);
	}
}
