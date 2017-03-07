package com.example.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

import com.example.api.common.exception.ApiDaoException;
import com.example.api.common.exception.ApiServiceException;
import com.example.api.common.utils.ApiCommonUtils;
import com.example.api.common.utils.ApiUrlBuilder;
import com.example.api.common.utils.LogHelper;
import com.example.api.common.utils.ResponseUtils;
import com.example.api.dao.ApiDAO;
import com.example.api.entities.request.ApiRequest;
import com.example.api.enums.ApiComponent;
import com.example.api.service.ApiComponentService;
import com.example.api.service.BaseApiService;


/**
 * Base of all Api services.
 *
 */
public abstract class BaseApiServiceImpl implements BaseApiService{

    @Autowired
    private ApiDAO apiDAO;
    
    @Autowired
    private ApiUrlBuilder urlBuilder;
    
    @Autowired
    private ApiCommonUtils apiUtils;
    
    @Autowired
	private ApiComponentService apiComponentService;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * All services must define which component they are.
     */
    protected abstract ApiComponent getComponent();

    
    @Override
    public boolean isCacheable() {
    	return this.getApiUtils().isCacheable(this.getComponent());
    }
    
    @Override
    public int cacheTtl() {
    	return this.getApiUtils().cacheTtl(this.getComponent());
    }
    
    @Override
    public String getApiKey() {
    	String apiKey = this.getApiUtils().getApiKey(this.getComponent());
    	
    	return apiKey;
    }
    
    @Override
    public ResponseEntity<String> get(ApiRequest apiRequest) throws ApiServiceException {
        ResponseEntity<String> apiResponse = null;
        try {
            apiResponse = this.getApiDAO().get(apiRequest);
        } catch (ApiDaoException e) {
            LogHelper.error(this, e);
        }
        
        return apiResponse;
    }

    @Override
    public ResponseEntity<String> head(ApiRequest apiRequest) throws ApiServiceException {
        ResponseEntity<String> apiResponse = null;
        try {
            apiResponse = this.getApiDAO().head(apiRequest);
        } catch (ApiDaoException e) {
            LogHelper.error(this, e);
        }
        
        return apiResponse;
    }

    @Override
    public ResponseEntity<String> post(ApiRequest apiRequest) throws ApiServiceException {
        ResponseEntity<String> apiResponse = null;
        try {
            apiResponse = this.getApiDAO().post(apiRequest);
        } catch (ApiDaoException e) {
            LogHelper.error(this, e);
        }
        
        return apiResponse;
    }

    @Override
    public ResponseEntity<String> patch(ApiRequest apiRequest) throws ApiServiceException {
        ResponseEntity<String> apiResponse = null;
        try {
            apiResponse = this.getApiDAO().patch(apiRequest);
        } catch (ApiDaoException e) {
            LogHelper.error(this, e);
        }
        
        return apiResponse;
    }

    @Override
    public ResponseEntity<String> put(ApiRequest apiRequest) throws ApiServiceException {
        ResponseEntity<String> apiResponse = null;
        try {
            apiResponse = this.getApiDAO().put(apiRequest);
        } catch (ApiDaoException e) {
            LogHelper.error(this, e);
        }
        
        return apiResponse;
    }

    @Override
    public ResponseEntity<String> delete(ApiRequest apiRequest) throws ApiServiceException {
        ResponseEntity<String> apiResponse = null;
        try {
            apiResponse = this.getApiDAO().delete(apiRequest);
        } catch (ApiDaoException e) {
            LogHelper.error(this, e);
        }
        
        return apiResponse;
    }

    @Override
    public ResponseEntity<String> options(ApiRequest apiRequest) throws ApiServiceException {
        ResponseEntity<String> apiResponse = null;
        try {
            apiResponse = this.getApiDAO().options(apiRequest);
        } catch (ApiDaoException e) {
            LogHelper.error(this, e);
        }
        
        return apiResponse;
    }


    @Override
    public <T> ResponseEntity<T>  get(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException {
        ResponseEntity<String> apiResponse = this.get(apiRequest);
        ResponseEntity<T> parsedData = this.createResponseEntityTyped(apiRequest,apiResponse,responseType);
        
        return parsedData;
    }

    @Override
    public <T> ResponseEntity<T>  head(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> ResponseEntity<T>  post(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException {
        ResponseEntity<String> apiResponse = this.post(apiRequest);
        ResponseEntity<T> parsedData = this.createResponseEntityTyped(apiRequest,apiResponse,responseType);
        return parsedData;
    }

    @Override
    public <T> ResponseEntity<T>  patch(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException {
        ResponseEntity<String> apiResponse = this.patch(apiRequest);
        ResponseEntity<T> parsedData = this.createResponseEntityTyped(apiRequest,apiResponse,responseType);
        return parsedData;
    }

    @Override
    public <T> ResponseEntity<T>  put(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException {
        ResponseEntity<String> apiResponse = this.put(apiRequest);
        ResponseEntity<T> parsedData = this.createResponseEntityTyped(apiRequest,apiResponse,responseType);
        return parsedData;
    }

    @Override
    public <T> ResponseEntity<T>  delete(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> ResponseEntity<T>  options(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /*
     * 
     * 
     * *********** Private methods *****************
     * 
     * 
     */
    
    private <T> ResponseEntity<T> createResponseEntityTyped(ApiRequest apiRequest, ResponseEntity<String> apiResponse, Class<T> responseType){
        BodyBuilder apiResponseBuilder=  ResponseEntity.status(apiResponse.getStatusCode());
        T parseData = null;
        
        if (apiResponse.getStatusCode().is2xxSuccessful()){
            parseData = ResponseUtils.parseData(apiResponse.getBody(), apiRequest.getResponseFormat(), responseType);
        }

        ResponseEntity<T> apiResponseTyped = apiResponseBuilder.headers(apiResponse.getHeaders()).body(parseData);
        
        return apiResponseTyped;
    }

    //Getters and setters
    public ApiDAO getApiDAO() {
        return this.apiDAO;
    }

    public void setApiDAO(ApiDAO apiDAO) {
        this.apiDAO = apiDAO;
    }

    public ApiUrlBuilder getUrlBuilder() {
        return this.urlBuilder;
    }

    public void setUrlBuilder(ApiUrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    public ApiCommonUtils getApiUtils() {
        return this.apiUtils;
    }

    public void setApiUtils(ApiCommonUtils apiUtils) {
        this.apiUtils = apiUtils;
    }


	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}


	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}


	public ApiComponentService getApiComponentService() {
		return this.apiComponentService;
	}


	public void setApiComponentService(ApiComponentService apiComponentService) {
		this.apiComponentService = apiComponentService;
	}

}
