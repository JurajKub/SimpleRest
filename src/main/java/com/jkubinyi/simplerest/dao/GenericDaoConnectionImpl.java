package com.jkubinyi.simplerest.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.jkubinyi.simplepool.database.Connection;
import com.jkubinyi.simplepool.database.ConnectionPool;
import com.jkubinyi.simplerest.exception.BusinessException;
import com.jkubinyi.simplerest.exception.BusinessException.Unchecked;

public class GenericDaoConnectionImpl<T extends Identifiable<ID>, ID> implements GenericDao<T, ID> {
	
	@Autowired
	private ConnectionPool pool;
	
	@Override
	public T save(T entity) {
		try {
			try(Connection connection = this.pool.getConnection()) {
				EntityModel entityModel = EntityModel.of(entity);
				if(this.executeUpsert(connection, entityModel)) {
					connection.commit();
					return entity;
				}
				else return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			Unchecked err = new BusinessException.Unchecked();
			err.initCause(e);
			err.setMessage("Problem during processing request");
			err.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			throw err;
		}
	}
	
	@Override
	public List<T> findAll() {
		try {
			try(Connection connection = this.pool.getConnection()) {
				Class<T> clazz = this.getEntityClass();
				Statement st = connection.createStatement();
				StringBuilder sb = new StringBuilder();
				List<T> result = new ArrayList<>();
				sb.append("SELECT * FROM `")
				.append(EntityModel.getTableName(clazz))
				.append("`");
				
				ResultSet resultSet = st.executeQuery(sb.toString());
				while(resultSet.next()) {
					T inst = this.resultSetToEntity(clazz, resultSet);
					result.add(inst);
				}
				return result;
			}
		} catch(Exception e) {
			e.printStackTrace();
			Unchecked err = new BusinessException.Unchecked();
			err.initCause(e);
			err.setMessage("Problem during processing request");
			err.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			throw err;
		}
	}

	@Override
	public void delete(T entity) {
	}

	@Override
	public Optional<T> findById(ID id) {
		try {
			try(Connection connection = this.pool.getConnection()) {
				Class<T> clazz = this.getEntityClass();
				String pkColumnName = EntityModel.columnsListOf(clazz, (field, column) -> {
					if(field.isAnnotationPresent(Id.class))
						return true;
					return false;
				}).get(0);
				
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT * FROM `")
				.append(EntityModel.getTableName(clazz))
				.append("`")
				.append(" WHERE `")
				.append(pkColumnName)
				.append("`=?");
				
				PreparedStatement st = connection.prepareStatement(sb.toString());
				Object value = null;
				if(id instanceof UUID) value = ((UUID) id).toString();
				else value = id;
				st.setObject(1, value);
				ResultSet resultSet = st.executeQuery();
				resultSet.next(); // Get only first result
				return Optional.ofNullable(this.resultSetToEntity(clazz, resultSet));
			}
		} catch(Exception e) {
			e.printStackTrace();
			Unchecked err = new BusinessException.Unchecked();
			err.initCause(e);
			err.setMessage("Problem during processing request");
			err.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			throw err;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	private T resultSetToEntity(Class<T> clazz, ResultSet resultSet) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<T> constructor = clazz.getConstructor(); // Entity must have a no-arg constructor
		constructor.setAccessible(true);
		T inst = constructor.newInstance();
		constructor.setAccessible(false);
		
		EntityModel.columnsConsumerOf(clazz, (field, column) -> {
			try {
				String fieldName = column.name();
				if(fieldName == null) fieldName = field.getName();
				field.setAccessible(true);
				if(field.getType().equals(UUID.class)) field.set(inst, UUID.fromString(resultSet.getString(fieldName)));
				else field.set(inst, resultSet.getObject(fieldName));
				field.setAccessible(false);
			} catch(SQLException | IllegalAccessException e) {
				Unchecked err = new BusinessException.Unchecked();
				err.initCause(e);
				err.setMessage("Problem during processing request");
				err.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
				throw err;
			}
		});
		return inst;
	}
	
	private boolean executeUpsert(Connection connection, EntityModel model) throws SQLException {
		int count = 0;
		try {
			PreparedStatement st = this.createUpdate(connection, model);
			count = st.executeUpdate();
		} catch(SQLException e) { } // We don't need to log SQLException here -> we will try to insert and see
		
		if(count <= 0) {
			PreparedStatement st = this.createInsert(connection, model);
			count = st.executeUpdate();
			if(count > 0) return true;
		} else return true;
		
		return false;
	}
	
	private PreparedStatement createUpdate(Connection connection, EntityModel model) throws SQLException {
		boolean first = true;
		List<Object> values = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE `")
		.append(model.getTable())
		.append("` SET ");
		
		for (Map.Entry<String, Object> entry : model.columns.entrySet()) {
			if(!first) sb.append(",");
			else first = false;
			
			sb.append("`")
			.append(entry.getKey())
			.append("`")
			.append("=?");
			
			values.add(entry.getValue());
		}
		
		sb.append(" WHERE `")
		.append(model.getPkColumnName())
		.append("`=?");
		values.add(model.getPkColumnValue());
		
		PreparedStatement st = connection.prepareStatement(sb.toString());
		for(int i=0; i<values.size(); i++) {
			st.setObject(i+1, values.get(i));
		}
		
		return st;
	}
	
	private PreparedStatement createInsert(Connection connection, EntityModel model) throws SQLException {
		List<Object> values = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `")
		.append(model.getTable())
		.append("` (`")
		.append(model.pkColumnName)
		.append("`");
		values.add(model.pkColumnValue);
		
		for (Map.Entry<String, Object> entry : model.columns.entrySet()) {
			sb.append(",")
			.append("`")
			.append(entry.getKey())
			.append("`");
			
			values.add(entry.getValue());
		}
		
		sb.append(") VALUES (");
		for(int i=0; i<values.size(); i++) {
			if(i != 0) sb.append(",");
			
			sb.append("?");
		}
		sb.append(")");
		String query = sb.toString();
		
		PreparedStatement st = connection.prepareStatement(query);
		for(int i=0; i<values.size(); i++) {
			Object value = values.get(i);
			if(value instanceof UUID) st.setObject(i+1, ((UUID)value).toString());
			else st.setObject(i+1, value);
		}
		
		return st;
	}
	
	public static class EntityModel {
		
		private final Map<String, Object> columns;
		private String pkColumnName;
		private Object pkColumnValue;
		private final String table;

		public static EntityModel of(Object entity) {
			Map<String, Object> columns = new LinkedHashMap<>();
			String tableName = null;
			Class<?> clazz = entity.getClass();
			tableName = EntityModel.getTableName(clazz);
			EntityModel entityModel = new EntityModel(columns, tableName);
			
			EntityModel.columnsConsumerOf(clazz, (field, column) -> {
				try {
					Id id = field.getAnnotation(Id.class);
					String fieldName = column.name();
					if(fieldName == null) fieldName = field.getName();
					field.setAccessible(true);
					Object result = field.get(entity);
					field.setAccessible(false);
					
					if(id == null)
						columns.put(fieldName, result);
					else {
						entityModel.setPkColumnName(fieldName);
						entityModel.setPkColumnValue(result);
					}
				} catch(IllegalAccessException e) {
				}
			});
			return entityModel;
		}
		
		public static List<String> columnsListOf(Class<?> clazz) {
			return EntityModel.columnsListOf(clazz, (field, column) -> {
				return true;
			});
		}
		
		public static List<String> columnsListOf(Class<?> clazz, BiFunction<Field, Column, Boolean> filterFunction) {
			List<String> fieldsList = new ArrayList<String>();
			EntityModel.columnsConsumerOf(clazz, (field, column) -> {
				String columnName = null;
				if(column.name() != null) columnName = column.name();
				else columnName = field.getName();
				
				if(filterFunction.apply(field, column)) fieldsList.add(columnName);
			});
			
			return fieldsList;
		}

		public static void columnsConsumerOf(Class<?> clazz, BiConsumer<Field, Column> fieldConsumer) {
			EntityModel.columnsConsumerOf(clazz, (field, column) -> {
				fieldConsumer.accept(field, column);
				return true;
			});
		}
		
		public static void columnsConsumerOf(Class<?> clazz, BiFunction<Field, Column, Boolean> fieldFunction) {
			EntityModel.fieldsConsumerOf(clazz, field -> {
				Column column = field.getAnnotation(Column.class);
				if(column != null) 
					if(!fieldFunction.apply(field, column)) return false;
				return true;
			});
		}
		
		public static void fieldsConsumerOf(Class<?> clazz, Consumer<Field> fieldConsumer) {
			for(Field field : clazz.getDeclaredFields()) {
				fieldConsumer.accept(field);
			}
		}
		
		public static void fieldsConsumerOf(Class<?> clazz, Function<Field, Boolean> fieldFunction) {
			for(Field field : clazz.getDeclaredFields()) {
				if(!fieldFunction.apply(field)) break;
			}
		}
		
		public static String getTableName(Object entity) {
			Class<?> clazz = entity.getClass();
			return EntityModel.getTableName(clazz);
		}
		
		public static String getTableName(Class<?> clazz) {
			String tableName = null;
			Table table = clazz.getAnnotation(Table.class);
			if(table != null) tableName = table.name();
			if(tableName == null) tableName = clazz.getName();
			return tableName;
		}
		
		private EntityModel(Map<String, Object> columns, String pkColumnName, Object pkColumnValue, String table) {
			this.columns = columns;
			this.pkColumnName = pkColumnName;
			this.pkColumnValue = pkColumnValue;
			this.table = table;
		}

		private EntityModel(Map<String, Object> columns, String table) {
			this.columns = columns;
			this.table = table;
		}

		public String getTable() {
			return table;
		}

		public Map<String, Object> getColumns() {
			return columns;
		}

		public String getPkColumnName() {
			return pkColumnName;
		}

		public Object getPkColumnValue() {
			return pkColumnValue;
		}

		public void setPkColumnName(String pkColumnName) {
			this.pkColumnName = pkColumnName;
		}

		public void setPkColumnValue(Object pkColumnValue) {
			this.pkColumnValue = pkColumnValue;
		}
	}

}
