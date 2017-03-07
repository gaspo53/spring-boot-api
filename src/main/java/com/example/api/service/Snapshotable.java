/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.service;

import org.springframework.stereotype.Service;

/**
 * Tells that a {@link Service} has to refresh its data and store for future querying.
 * @author gaspar
 *
 */
public interface Snapshotable {

	/**
	 * Refresh the data
	 */
	void refresh();
	
	/**
	 * Indicates if the snapshot function is enabled or not
	 * @return
	 */
	default boolean enabled(){
		return true;
	}
	
}
