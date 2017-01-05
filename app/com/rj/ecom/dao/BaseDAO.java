package com.rj.ecom.dao;

import java.util.List;

public interface BaseDAO<T> {

		public T insert(T obj);
		
		public void update(T obj);
		
		public void delete(T obj);
		
		public T findById(long id , Class className);
		
		public List<T> findAll(T object);
		 
	}
