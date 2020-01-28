package com.jkubinyi.simplerest.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jkubinyi.simplerest.dao.BookDao;
import com.jkubinyi.simplerest.dto.BookDto;
import com.jkubinyi.simplerest.entity.Book;
import com.jkubinyi.simplerest.exception.NullNotEnabledException;

@Service
public class BookService {

	@Autowired
	@Qualifier("bookJpa")
	private BookDao bookDao;
	
	public Optional<BookDto> getBookById(UUID id) {
		Optional<Book> optionalBook = this.bookDao.findById(id);
		if(optionalBook.isPresent()) {
			Book book = optionalBook.get();
			String genre = "";
			if(book.getGenres().size() != 0)
				genre = book.getGenres().stream().reduce(", ", String::concat);
			return Optional.of(new BookDto(book.getId(), genre, book.getAuthor(), book.getTitle(), book.getYear()));
		}
		
		return Optional.empty();
	}
	
	public List<BookDto> getAllBooks() {
		return this.bookDao.findAll()
				.stream()
				.map(book -> new BookDto(book.getId(), 
						book.getGenres().stream().reduce(", ", String::concat), 
						book.getAuthor(), 
						book.getTitle(), 
						book.getYear()))
				.collect(Collectors.toUnmodifiableList());
	}
	
	public Optional<Book> insertBook(BookDto book) {
		if(book == null) throw new NullNotEnabledException();
		Book bookEntity = new Book(book.getTitle(), book.getAuthor(), book.getYear());
		bookEntity.setId(UUID.randomUUID());
		return Optional.ofNullable(this.bookDao.save(bookEntity));
	}
}
