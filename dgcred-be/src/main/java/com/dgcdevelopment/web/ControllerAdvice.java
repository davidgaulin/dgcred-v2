package com.dgcdevelopment.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.dgcdevelopment.domain.exceptions.DataConflictException;
import com.dgcdevelopment.domain.exceptions.InvalidInformationException;
import com.dgcdevelopment.domain.exceptions.MissingEntityException;
import com.dgcdevelopment.domain.exceptions.MissingInformationException;
import com.dgcdevelopment.domain.exceptions.UnauthenticatedException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
	
	@ExceptionHandler(Exception.class) 
	public void handleAll(Exception ex, WebRequest request, HttpServletResponse response) 
		throws IOException {
		if (ex != null) {
			if (ex instanceof MissingInformationException
						|| ex instanceof InvalidInformationException) {
			    response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
			} else if (ex instanceof MissingEntityException) {
			    response.sendError(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
			} else if (ex instanceof UnauthenticatedException) {
				response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getLocalizedMessage());
			} else if (ex instanceof DataConflictException) {
				response.sendError(HttpStatus.CONFLICT.value(), ex.getLocalizedMessage());
			} else {
				System.out.println(ex.getLocalizedMessage());
				ex.printStackTrace();
				response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "WTF???");
			}
		} else {
			response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "WTF???");
		}
	}

}
