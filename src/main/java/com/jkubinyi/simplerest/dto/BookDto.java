package com.jkubinyi.simplerest.dto;

import java.util.UUID;

public class BookDto {
	
	private final String author;
	private final String title;
	private final String genre;
	private final UUID id;
	private final int year;
	/**
	 * @param author
	 * @param title
	 * @param year
	 */
	public BookDto(UUID id, String genre, String author, String title, int year) {
		super();
		this.id = id;
		this.genre = genre;
		this.author = author;
		this.title = title;
		this.year = year;
	}
	public String getGenre() {
		return genre;
	}
	public String getAuthor() {
		return author;
	}
	public String getTitle() {
		return title;
	}
	public int getYear() {
		return year;
	}
	public UUID getId() {
		return id;
	}

}