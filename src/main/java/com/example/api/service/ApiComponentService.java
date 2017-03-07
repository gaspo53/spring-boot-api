/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.service;

import com.example.api.enums.ApiComponent;

/**
 * Interface to decribe common operations for {@link ApiComponent}.
 * @author gaspar
 *
 */
public interface ApiComponentService {

	/**
	 * Tells if the received {@link ApiComponent} is enabled to perform executions.
	 * @param apiComponent
	 * @return
	 */
	boolean isEnabled(ApiComponent apiComponent);
	
}
