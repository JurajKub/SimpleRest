package com.jkubinyi.simplerest.dto;

public class BusinessFault {

	private String message;
	private String meaning;
	private String details;
	private long timestamp;
	/**
	 * @param message
	 * @param meaning
	 * @param details
	 * @param timestamp
	 */
	public BusinessFault(String message, String meaning, String details, long timestamp) {
		super();
		this.message = message;
		this.meaning = meaning;
		this.details = details;
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMeaning() {
		return meaning;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
