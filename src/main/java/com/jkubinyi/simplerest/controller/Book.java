package com.jkubinyi.simplerest.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkubinyi.simplerest.dto.AcknowledgedDto;
import com.jkubinyi.simplerest.dto.BookDto;
import com.jkubinyi.simplerest.service.BookService;

@RestController
public class Book {

	@Autowired
	private BookService bookService;
	
	private Logger log = LoggerFactory.getLogger(Book.class);
	
	@GetMapping("/books")
	public List<BookDto> getAllBooks() {
		return this.bookService.getAllBooks();
	}
	
	@PostMapping("/book")
	public ResponseEntity<AcknowledgedDto> addBook(@RequestBody BookDto book) {
		Optional<com.jkubinyi.simplerest.entity.Book> optionalBook = this.bookService.insertBook(book);
		if(optionalBook.isPresent()) return new ResponseEntity<AcknowledgedDto>(new AcknowledgedDto(optionalBook.get().getId()), HttpStatus.OK);
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<BookDto> getBookById(@PathVariable("id") UUID id) {
		log.info("{}", id);
		Optional<BookDto> optionalBook = this.bookService.getBookById(id);
		if(optionalBook.isPresent()) {
			return new ResponseEntity<BookDto>(optionalBook.get(), HttpStatus.OK);
		} else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
