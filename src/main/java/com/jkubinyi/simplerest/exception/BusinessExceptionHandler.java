package com.jkubinyi.simplerest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jkubinyi.simplerest.dto.BusinessFault;

@RestControllerAdvice
public class BusinessExceptionHandler {

	@ExceptionHandler({BusinessException.Checked.class})
	public ResponseEntity<BusinessFault> businessCheckedExceptionCaught(BusinessException.Checked ex) {
		return new ResponseEntity<BusinessFault>(
				new BusinessFault(ex.getMessage(), ex.getStatusCode().getReasonPhrase(), ex.getReadableCause(), System.currentTimeMillis())
				, ex.getStatusCode());
	}

	@ExceptionHandler({BusinessException.Unchecked.class})
	public ResponseEntity<BusinessFault> businessUncheckedExceptionCaught(BusinessException.Unchecked ex) {
		return new ResponseEntity<BusinessFault>(
				new BusinessFault(ex.getMessage(), ex.getStatusCode().getReasonPhrase(), ex.getReadableCause(), System.currentTimeMillis())
				, ex.getStatusCode());
	}
}
