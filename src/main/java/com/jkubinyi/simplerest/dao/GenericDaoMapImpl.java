package com.jkubinyi.simplerest.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GenericDaoMapImpl<T extends Identifiable<ID>, ID> implements GenericDao<T, ID> {
	
	private Map<ID, T> cache = new ConcurrentHashMap<>();

	@Override
	public T save(T entity) {
		this.cache.put(entity.getId(), entity);
		return entity;
	}

	@Override
	public List<T> findAll() {
		List<T> items = new ArrayList<>();
		this.cache.forEach((key, value) -> {
			items.add(value);
		});
		return items;
	}

	@Override
	public void delete(T entity) {
		Objects.requireNonNull(entity, "Entity cannot be null.");
		this.cache.remove(entity.getId());
	}

	@Override
	public Optional<T> findById(ID id) {
		return Optional.ofNullable(this.cache.get(id));
	}

}
