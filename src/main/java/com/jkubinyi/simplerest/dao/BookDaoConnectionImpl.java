package com.jkubinyi.simplerest.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.jkubinyi.simplerest.entity.Book;

@Repository("bookJpa")
public class BookDaoConnectionImpl extends GenericDaoConnectionImpl<Book, UUID> implements BookDao {
}
