package com.jkubinyi.simplerest.exception;

import org.springframework.http.HttpStatus;

import com.jkubinyi.simplerest.exception.BusinessException.Unchecked;

public class NullNotEnabledException extends Unchecked {
	
	private static final long serialVersionUID = 1L;

	public NullNotEnabledException() {
		this(null);
	}
	
	public NullNotEnabledException(String cause) {
		this.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
		this.setMessage("Record is invalid. Please see details for more information.");
		this.setCause(cause);
	}

}
