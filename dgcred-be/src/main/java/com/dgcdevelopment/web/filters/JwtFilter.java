package com.dgcdevelopment.web.filters;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
@CrossOrigin
public class JwtFilter implements Filter {
	
	@Autowired
	private UserRepository userRepo;
	
    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        	
        	// UNITL we can figure out the JWT Login issue.  Adding default user
        	// TODO INVALID CODE SECURITY WARNING AAHAHHAHAHAH
        	User u = userRepo.findOneByUsername("a@a.com");
        	String b = Jwts.builder().setSubject(u.getUsername()).claim("roles", u.getRoles()).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secretkey").compact();
        	authHeader = "Bearer " + b;
        	// TODO INVALID CODE SECURITY WARNING AAHAHHAHAHAH        	
        	
        	System.out.println("Didn't get Authorization header with:");
        	System.out.println("	URL:" + request.getRequestURL());
        	System.out.println("	Method:" + request.getMethod());
//        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        	return;
        }
        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
        	// TODO delete System.out
        	System.out.println(token);
            final Claims claims = Jwts.parser().setSigningKey("secretkey")
                .parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
            request.setAttribute("user", getUser(claims));
            chain.doFilter(req, res);
        }
        catch (final SignatureException e) {
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	//request.setAttribute("user", userRepo.findOneByUsername("a@a.com"));
        }
        
    }
    
    public void init(FilterConfig filterConfig) {
    	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
    }
    public void destroy() {}
    
	private User getUser(Claims claim) throws ServletException {
		List<User> us = userRepo.findByUsername(claim.getSubject());
		User user = null;
		if (us.size() == 1) {
			user = us.get(0);
		} else {
			throw new ServletException("User not found!");
		}
		return user;
	}
}