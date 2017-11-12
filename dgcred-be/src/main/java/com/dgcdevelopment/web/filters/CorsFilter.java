package com.dgcdevelopment.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;


@Component
@CrossOrigin
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        
        HttpServletRequest request = (HttpServletRequest) req;
        
        System.out.println("origin:" + request.getHeader("origin"));
        
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "x-request-with, authorization, Origin, X-Requested-With, Content-Type, Accept, X-PINGOVER");
        //response.setHeader("Access-Control-Allow-Headers", "x-request-with, authorization");
        response.setHeader("Access-Control-Max-Age", "86400");
        
        if (!"OPTIONS".equals(request.getMethod())) {
        	chain.doFilter(req, res);
        }
    }
    
    public void init(FilterConfig filterConfig) {}
    public void destroy() {}

}