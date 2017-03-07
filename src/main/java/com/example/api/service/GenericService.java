package com.example.api.service;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.data.mongodb.core.query.Query;

import com.example.api.common.exception.BusinessException;
import com.example.api.common.filter.Filter;
import com.example.api.common.filter.Page;
import com.example.api.common.filter.Result;

/**
 * Generic Service defining common operations.<br>
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 * @param <E>
 */
public interface GenericService<E> {

	/**
	 * Returns a Result containing the objects that match the current filter.
	 * @param filter
	 * @param p
	 * @see {@link Filter}
	 * @return Result
	 */
	Result<E> search(Filter filter, Page p);

	/**
	 * Returns a Result containing the objects that match the current query.
	 * @param filter
	 * @param p
	 * @see {@link Queryr}
	 * @return Result
	 */
	Result<E> search(Query query);

	/**
	 * Returns a Result containing the objects that match the current filter.
	 * @param filter
	 * @param p
	 * @see {@link Filter}
	 * @return Result
	 */
	Result<E> searchWithReferences(Filter filter, Page p);
	
	/**
	 * Gets the object with the id received, in a non-eager fetch.
	 * @param id
	 * @return
	 */
	E getById(Serializable id);

	/**
	 * Gets the object with the id received, and all the referenced objects (eager fetch).
	 * @param id
	 * @return E. Null if not found
	 */
	E getByIdWithReferences(Serializable id);

	/**
	 * Saves or updates (if exists) the object in the DAO. 
	 * @param object
	 * @return
	 */
	E save(E o) throws BusinessException;

	/**
	 * Saves or updates (if exists) each object of collection in the DAO. 
	 * @param object
	 * @return
	 */
	Collection<E> save(Collection<E> collection) throws BusinessException;

	/**
	 * Deletes the object with the received ID, and all it's referenced objects.
	 * @param object
	 */
	void delete(Serializable id);

	/**
	 * Deletes the object, and all it's referenced objects.
	 * @param object
	 */
	void delete(E o);

	/**
	 * Returns the count of the total objects matching the current filter.
	 * @param filter
	 * @return
	 * @throws BusinessException
	 */
	Long count(Filter filter);
	
	/**
	 * Returns the entire collection.
	 * @return
	 */
	Result<E> getAll();

	/**
	 * Get all, and all the referenced objects (eager fetch).
	 * @return Result
	 */
	Result<E> getAllWithReferences() throws BusinessException;
	
	/**
	 * Deletes all elements from the collection.<br>
	 * <b>Warning: it deletes ALL ELEMENTS without a query</b>
	 */
	void deleteAll();
}
