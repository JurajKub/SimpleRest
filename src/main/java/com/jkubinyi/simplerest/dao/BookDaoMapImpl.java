package com.jkubinyi.simplerest.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.jkubinyi.simplerest.entity.Book;

@Repository("bookInMemory")
public class BookDaoMapImpl extends GenericDaoMapImpl<Book, UUID> implements BookDao {
}