package com.jkubinyi.simplerest.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T extends Identifiable<ID>, ID> {
	public T save(T entity);
	public List<T> findAll();
	public void delete(T entity);
	public Optional<T> findById(ID id);
}
