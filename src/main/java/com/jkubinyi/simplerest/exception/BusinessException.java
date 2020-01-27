package com.jkubinyi.simplerest.exception;

import org.springframework.http.HttpStatus;

public class BusinessException {

	private BusinessException() { }
	
	public static class Checked extends Exception {
		
		private HttpStatus code;
		private String message;
		private String cause;

		private static final long serialVersionUID = 1L;
		
		public Checked setStatusCode(HttpStatus code) {
			this.code = code;
			return this;
		}
		
		public Checked setMessage(String message) {
			this.message = message;
			return this;
		}
		
		public Checked setCause(String cause) {
			this.cause = cause;
			return this;
		}
		
		public static Checked create(HttpStatus code, String message, String cause) {
			return new Checked().setStatusCode(code).setMessage(message).setCause(cause);
		}
		
		public HttpStatus getStatusCode() {
			return this.code;
		}
		
		public String getReadableCause() {
			return this.cause;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
	
	public static class Unchecked extends RuntimeException {
		
		private HttpStatus code;
		private String message;
		private String cause;

		private static final long serialVersionUID = 1L;
		
		public Unchecked setStatusCode(HttpStatus code) {
			this.code = code;
			return this;
		}
		
		public Unchecked setMessage(String message) {
			this.message = message;
			return this;
		}
		
		public Unchecked setCause(String cause) {
			this.cause = cause;
			return this;
		}
		
		public static Unchecked create(HttpStatus code, String message, String cause) {
			return new Unchecked().setStatusCode(code).setMessage(message).setCause(cause);
		}
		
		public HttpStatus getStatusCode() {
			return this.code;
		}
		
		public String getReadableCause() {
			return this.cause;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
}
