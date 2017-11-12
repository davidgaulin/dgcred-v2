package com.dgcdevelopment.domain.exceptions;

public class DataConflictException extends Exception {

	private static final long serialVersionUID = 5979984952353488368L;

	public DataConflictException(String ex) {
		super(ex);
	}
}
