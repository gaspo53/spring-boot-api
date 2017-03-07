/**
 * @author Gaspar Rajoy
 */
package com.example.api.dao;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.example.api.common.exception.ApiDaoException;
import com.example.api.connector.ApiConnector;
import com.example.api.entities.request.ApiRequest;

/**
 * Wrapper of {@link ApiConnector} for implementing DAOs making API calls.
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
public interface ApiDAO {

    /**
     * Makes a {@link HttpMethod#GET} to ApiV3
     * @param apiRequest
     * @return
     * @throws ApiDaoException
     */
    ResponseEntity<String> get(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> head(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> post(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> patch(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> put(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> delete(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> options(ApiRequest apiRequest) throws ApiDaoException;

}
