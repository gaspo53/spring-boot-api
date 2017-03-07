/**
 * @autor @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 */
package com.example.api.service;

import com.example.api.enums.ApiComponent;

/**
 * Interface to implements different strategies to resolve an api key
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
public interface ApiKeyResolverService {

    /**
     * Resolves the API key for {@link ApiComponent}
     * @param apiRequest
     * @return
     */
	String getApiKey(ApiComponent component);
}
