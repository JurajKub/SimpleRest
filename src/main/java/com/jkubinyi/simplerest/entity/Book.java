package com.jkubinyi.simplerest.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jkubinyi.simplerest.dao.Identifiable;

@Entity
@Table(name = "book")
public class Book implements Identifiable<UUID> {

	@Id
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "title", unique = true, updatable = false)
	private String title;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "year")
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

}
