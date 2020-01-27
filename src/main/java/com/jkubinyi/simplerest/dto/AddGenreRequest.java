package com.jkubinyi.simplerest.dto;

import java.util.UUID;

public class AddGenreRequest {

	private UUID groupId;
	private UUID bookId;
	
	/**
	 * @param groupId
	 * @param bookId
	 */
	public AddGenreRequest(UUID groupId, UUID bookId) {
		super();
		this.groupId = groupId;
		this.bookId = bookId;
	}
	
	public UUID getGroupId() {
		return groupId;
	}
	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
	}
	public UUID getBookId() {
		return bookId;
	}
	public void setBookId(UUID bookId) {
		this.bookId = bookId;
	}
}
