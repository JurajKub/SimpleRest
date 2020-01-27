package com.jkubinyi.simplerest.dto;

import java.util.UUID;

public class GenreDto {
	
	private UUID id;
	
	private String name;
	
	public GenreDto() {
	}

	/**
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	public GenreDto(UUID id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
