package com.example.api.dao;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.api.common.exception.DataAccessException;
import com.example.api.common.filter.Filter;
import com.example.api.common.filter.Page;
import com.example.api.common.filter.Result;

/**
 * Generic DAO interface defining common operations.<br>
 * Any custom DAO must implement this interface.
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 * @param <E>
 */
@Repository
public interface GenericDAO<E> {
	
	/**
	 * Save the object in the storage. 
	 * @param object
	 * @return
	 */
	E save(E object);
	
	/**
	 * Save the {@link Collection} in the storage. 
	 * @param object
	 * @return
	 */
	Collection<E> save(Collection<E> object);
	
	/**
	 * Get the object with the id received, in a non eager fetch.
	 * @param id
	 * @return
	 */
	E getById(Serializable id);
	
	/**
	 * Get the object with the id received, and all the referenced objects (eager fetch).
	 * @param id
	 * @return
	 */
	E getByIdWithReferences(Serializable id);
	
	/**
	 * Return a Result containing the objects that match the current filter.
	 * @param filter
	 * @param p
	 * @see {@link Filter}
	 * @return Result
	 */
	Result<E> search(Filter filter, Page p);

	/**
	 * Return a Result containing the objects that match the current query.
	 * @param filter
	 * @param p
	 * @see {@link Query}
	 * @return Result
	 */
	Result<E> search(Query query);

	/**
	 * Return a Result containing the objects that match the current filter, 
	 * and all the referenced objects (eager fetch).
	 * @param filter
	 * @param p
	 * @see {@link Filter}
	 * @return Result
	 */
	Result<E> searchWithReferences(Filter filter, Page p);
	
	/**
	 * Deletes the object, and all it's referenced objects (cascade all).
	 * @param object
	 */
	void delete (E object);
	
	/**
	 * Deletes the object with the id received, and all it's referenced object (cascade all).
	 * @param object
	 */
	void delete (Serializable id);

	/**
	 * Returns the count of the total objects matching the current filter.
	 * @param filter
	 * @throws DataAccessException
	 */
	Long count(Filter filter) throws DataAccessException;
	
	/**
	 * Returns the entire collection.
	 * @return Result
	 */
	Result<E> getAll();

	/**
	 * Get all, and all the referenced objects (eager fetch)
	 * @return Result
	 */
	Result<E> getAllWithReferences() throws DataAccessException;
	
	/**
	 * Fetch the object's references (if any)
	 */
	
	void getReferences(E object);

	/**
	 * Returns true if an object with the id received exists.
	 * @param id
	 * @return
	 */
	boolean exists(Serializable id);
	
	
	/**
	 * Deletes all elements from the collection.<br>
	 * <b>Warning: it deletes ALL ELEMENTS without a query</b>
	 */
	void deletaAll();

}
