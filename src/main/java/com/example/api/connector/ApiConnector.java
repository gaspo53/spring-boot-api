package com.example.api.connector;

import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.http.ResponseEntity;

import com.example.api.common.exception.ApiDaoException;
import com.example.api.entities.request.ApiRequest;

/**
 * Interface that any implementation of an Apiv3 connector  must implement.
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
public interface ApiConnector {

	ResponseEntity<String> get(ApiRequest apiRequest) throws ApiDaoException;
	
	ResponseEntity<String> head(ApiRequest apiRequest) throws ApiDaoException;
	
	ResponseEntity<String> post(ApiRequest apiRequest) throws ApiDaoException;

	ResponseEntity<String> patch(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> put(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> delete(ApiRequest apiRequest) throws ApiDaoException;

    ResponseEntity<String> options(ApiRequest apiRequest) throws ApiDaoException;

    /** Makes the real ApiCall with the {@link HttpRequestBase} mehod received.
     * 
     * @param url
     * @param client
     * @param method
     * @author gaspar
     */
	ResponseEntity<String> apiCall(ApiRequest apiRequest, HttpRequestBase method);
    
}