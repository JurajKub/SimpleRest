package com.jkubinyi.simplerest.exception;

import org.springframework.http.HttpStatus;

import com.jkubinyi.simplerest.exception.BusinessException.Checked;

public class RecordNotFoundException extends Checked {
	
	private static final long serialVersionUID = 1L;

	public RecordNotFoundException() {
		this(null);
	}
	
	public RecordNotFoundException(String cause) {
		this.setStatusCode(HttpStatus.NOT_FOUND);
		this.setMessage("Record was not found. Please see details for more information.");
		this.setCause(cause);
	}

}
