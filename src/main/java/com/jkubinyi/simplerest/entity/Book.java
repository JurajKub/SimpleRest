package com.jkubinyi.simplerest.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jkubinyi.simplerest.dao.Identifiable;

public class Book implements Identifiable<UUID> {
	
	private UUID id;
	
	private String title;
	
	private String author;
	
	private int year;
	
	private List<String> genre = new ArrayList<>();
	
	public Book() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param author
	 * @param year
	 */
	public Book(String title, String author, int year) {
		super();
		this.title = title;
		this.author = author;
		this.year = year;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public List<String> getGenres() {
		return genre;
	}
	
	public void addGenre(String genre) {
		this.genre.add(genre);
	}
	
	public void removeGenre(String genre) {
		this.genre.remove(genre);
	}

}
