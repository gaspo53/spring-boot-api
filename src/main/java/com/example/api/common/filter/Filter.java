package com.example.api.common.filter;

import org.springframework.data.mongodb.core.query.Query;

/**
 * Filter interface to build customs queries.<br>
 * Only works with Spring Data MongoDB
 *
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
public interface Filter {

	
	/**
	 * Fills the current query, according with values in the filter.
	 * @param criteria
	 */
	public void fillQuery(Query query);
}
