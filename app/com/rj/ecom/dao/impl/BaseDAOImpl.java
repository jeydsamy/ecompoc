package com.rj.ecom.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.rj.ecom.dao.BaseDAO;

import play.db.jpa.JPAApi;

public class BaseDAOImpl<T> implements BaseDAO<T>{

	@Inject JPAApi jpaApi;

	public EntityManager getEntityManager() {
		return jpaApi.em();
	}

	protected Criteria createExecutableCriteria(DetachedCriteria dc) {
		Criteria criteria = dc.getExecutableCriteria(getEntityManager().unwrap(Session.class));
		criteria.setCacheable(true);
		criteria.setCacheMode(CacheMode.NORMAL);
		return criteria;
	}

	public T insert(T obj) {
		getEntityManager().persist(obj);
		return obj;
	}

	public void update(T obj) {
		getEntityManager().merge(obj);
	}

	public void delete(T obj) {
		getEntityManager().remove(obj);
	}

	public T findById(long id, Class className){
		    return (T) getEntityManager().find(className, id);
	}
	
    @SuppressWarnings("unchecked")
    public List<T> findAll(T object) {
    	String sql = "Select t from " + object.getClass().getSimpleName() + " t";
            return getEntityManager().createQuery(sql).getResultList();
    }
}
