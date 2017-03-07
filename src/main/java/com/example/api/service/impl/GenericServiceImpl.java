package com.example.api.service.impl;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;

import com.example.api.common.exception.BusinessException;
import com.example.api.common.exception.DataAccessException;
import com.example.api.common.filter.Filter;
import com.example.api.common.filter.Page;
import com.example.api.common.filter.Result;
import com.example.api.common.utils.LogHelper;
import com.example.api.dao.GenericDAO;
import com.example.api.service.GenericService;

public abstract class GenericServiceImpl<E> implements GenericService<E> {

	@Autowired
	private GenericDAO<E> dao;

	@Override
	public E getById(Serializable id) {
		return this.getDao().getById(id);
	}

	@Override
	public Result<E> search(Query query) {
		Result<E> result = null;
		try {
			result = this.getDao().search(query);
		} catch (Exception e) {
			LogHelper.error(this, e);
		}	
		return result;
	}
	
	@Override
	public Result<E> search(Filter filter, Page p){
		Result<E> result = null;
		try {
			result = this.getDao().search(filter, p);
		} catch (Exception e) {
			LogHelper.error(this, e);
		}	
		return result;
	}
	
	@Override
	public E save(E o) throws BusinessException{
		return this.getDao().save(o);
	}

	@Override
	public Collection<E> save(Collection<E> collection) throws BusinessException{
		return this.getDao().save(collection);
	}

	
	@Override
	public void delete(E o){
		this.getDao().delete(o);
	}

	@Override
	public void delete(Serializable id){
		E o = this.getDao().getById(id);
		this.getDao().delete(o);
	}
	
	@Override
	public Long count(Filter filter) {
		long count = 0;
		
		try{
			count = this.getDao().count(filter);
		}
		catch(Exception e){
			LogHelper.error(this, e);
		}
		
		return count;
	}

	@Override
	public Result<E> getAll(){
		Result<E> result = null;
		try{
			result = this.getDao().getAll();
		}
		catch(Exception e){
			LogHelper.error(this, e);
			result = new Result<E>();
		}
		return result;
	}

	@Override
	public Result<E> searchWithReferences(Filter filter, Page p) {
		Result<E> result = null;
		try {
			result = this.getDao().searchWithReferences(filter, p);
		} catch (Exception e) {
			LogHelper.error(this, e);
		}			
		return result;
	}

	@Override
	public E getByIdWithReferences(Serializable id){
		return this.getDao().getByIdWithReferences(id);
	}

	@Override
	public Result<E> getAllWithReferences() {
		Result<E> result = null;
		try {
			result = this.getDao().getAllWithReferences();
		} catch (DataAccessException e) {
			LogHelper.error(this, e);
		}
		return result;
	}
	
	@Override
	public void deleteAll() {
		this.getDao().deletaAll();
	}
	
	//Getters and setters
	public GenericDAO<E> getDao() {
		return this.dao;
	}

	public void setDao(GenericDAO<E> dao) {
		this.dao = dao;
	}
	
}
