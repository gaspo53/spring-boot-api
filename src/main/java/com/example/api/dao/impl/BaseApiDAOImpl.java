package com.example.api.dao.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.example.api.common.exception.ApiDaoException;
import com.example.api.connector.ApiConnector;
import com.example.api.connector.impl.ApiConnectorImpl;
import com.example.api.dao.ApiDAO;
import com.example.api.entities.request.ApiRequest;

/**
 * Default implementation of {@link ApiDAO}, using {@link ApiConnectorImpl} as ApiV3 connector.
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
@Repository
public class BaseApiDAOImpl implements ApiDAO {
	
	@Autowired
    private ApiConnector connector;

    @Override
    public ResponseEntity<String> get(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;
        apiResponse = this.getConnector().get(apiRequest);
        return apiResponse;
    }


    @Override
    public ResponseEntity<String> head(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;
        apiResponse = this.getConnector().head(apiRequest);
        return apiResponse;
    }


    @Override
    public ResponseEntity<String> post(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;
        apiResponse = this.getConnector().post(apiRequest);
        return apiResponse;
    }


    @Override
    public ResponseEntity<String> patch(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;
        apiResponse = this.getConnector().patch(apiRequest);
        return apiResponse;
    }


    @Override
    public ResponseEntity<String> put(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;
        apiResponse = this.getConnector().put(apiRequest);
        return apiResponse;
    }


    @Override
    public ResponseEntity<String> delete(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;
        apiResponse = this.getConnector().delete(apiRequest);
        return apiResponse;
    }


    @Override
    public ResponseEntity<String> options(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;
        apiResponse = this.getConnector().options(apiRequest);
        return apiResponse;
    }
    
    
    
    /*
     * 
     * 
     * 
     * 
     * 
     ******** Protected and Private methods *************
     *
     * 
     * 
     * 
     */
    
    protected String getParameters(Map<String, String> params) {
        String url = "";
        Iterator<Entry<String, String>> it = params.entrySet().iterator();
        url += "?";
        while (it.hasNext()) {
            Entry<String, String> param = it.next();
            url += param.getKey() + "=" + param.getValue().toString().replaceAll(" ", "%20");
            if (it.hasNext()) {
                url += "&";
            }
        }
        return url;
    }
    
    //Getters and setters
    public ApiConnector getConnector() {
        return this.connector;
    }


    public void setConnector(ApiConnector downloader) {
        this.connector = downloader;
    }
    
}
