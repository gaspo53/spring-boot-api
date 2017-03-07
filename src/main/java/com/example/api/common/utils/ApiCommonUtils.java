package com.example.api.common.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.api.entities.request.ApiRequest;
import com.example.api.entities.response.ApiResponse;
import com.example.api.entities.response.ApiResponseStatus;
import com.example.api.enums.ApiComponent;
import com.example.api.service.ApiKeyResolverService;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Component
public class ApiCommonUtils {
    
    @Autowired
    private ApiKeyResolverService apiKeyResolverService;
    
    @Autowired
    private CommonUtils commonUtils;
    
    @Autowired
    private Environment environment;
    
    private Map<ApiComponent, Boolean> componentCacheableMap;
    private Map<ApiComponent, Integer> componentCacheTtlMap;
    private Map<ApiComponent, Integer> componentThresholdResetMap;
    
    
    @PostConstruct
    private void initMaps(){
    	this.componentCacheableMap = new HashMap<>();
    	this.componentCacheTtlMap = new HashMap<>();
    	this.componentThresholdResetMap = new HashMap<>();
    	
    	try{
    		List<ApiComponent> components = Arrays.asList(ApiComponent.values());
    		
    		components.forEach(component -> {
    			String cacheableKey = component.toString().concat(".cache.enabled");
    			String cacheTtlKey = component.toString().concat(".cache.ttl_seconds");
    			String thresholdResetKey = component.toString().concat(".threshold.reset_minutes");
    			
    			Boolean isCacheable = this.getCommonUtils().getProperty(cacheableKey, Boolean.class);
    			int cacheTtl = this.getCommonUtils().getProperty(cacheTtlKey, Integer.class);
    			int thresholdResetMinutes = this.getCommonUtils().getProperty(thresholdResetKey, Integer.class);
    			
    			this.componentCacheableMap.put(component, isCacheable);
    			this.componentCacheTtlMap.put(component, cacheTtl);
    			this.componentThresholdResetMap.put(component, thresholdResetMinutes);
    		});
    		
    	}catch(Exception e){
    		LogHelper.warn(this, e);
    	}
    }
    
    public void setApiProperties(ApiRequest apiRequest) {
    	//TODO
    }

    public String getApiKey(ApiComponent component) {
        String apiKey = this.getApiKeyResolverService().getApiKey(component);
        
        return apiKey;
    }

    public Integer thresholdReset(ApiComponent component){
    	int thresholdReset = -1;
    	
    	try{
			thresholdReset = this.componentThresholdResetMap.get(component);
    	}catch(Exception e){
    		LogHelper.error(this, e);
    	}
    	
    	return thresholdReset;
    }

    public Integer cacheTtl(ApiComponent component){
    	int cacheTtl = -1;
    	
    	try{
    		if (this.isCacheable(component)){
    			cacheTtl = this.componentCacheTtlMap.get(component);
    		}
    	}catch(Exception e){
    		LogHelper.error(this, e);
    	}
    	
    	return cacheTtl;
    }

    public boolean isCacheable(ApiComponent component) {
    	boolean isCacheable = false;
    	
    	try{
    		isCacheable = this.componentCacheableMap.get(component);
    	}catch(Exception e){
    		LogHelper.error(this, e);
    	}
    	
        return isCacheable;
    }

    /**
     * @param entity
     * @param apiResponse
     */
    public void setApiResponseStatus(String responseContent, ApiResponse<String> apiResponse) {
        try {
            if (apiResponse.getHttpStatus() != HttpStatus.OK.value()) {
                ApiResponseStatus apiResponseStatus = 
                		ResponseUtils.parseData(responseContent, 
                								PropertyNamingStrategy.SNAKE_CASE, 
                								ApiResponseStatus.class);
                apiResponseStatus.setStatus(apiResponse.getHttpStatus());
                apiResponse.setApiStatus(apiResponseStatus);
            }
        } catch (Exception e) {
            ApiResponseStatus apiResponseStatus = new ApiResponseStatus();
            apiResponseStatus.setStatus(apiResponse.getHttpStatus());
            apiResponse.setApiStatus(apiResponseStatus);
        }
    }



    // Getters and setters
    public ApiKeyResolverService getApiKeyResolverService() {
        return this.apiKeyResolverService;
    }


    public void setApiKeyResolverService(ApiKeyResolverService apiKeyResolverService) {
        this.apiKeyResolverService = apiKeyResolverService;
    }

	public Environment getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}


	public CommonUtils getCommonUtils() {
		return this.commonUtils;
	}


	public void setCommonUtils(CommonUtils commonUtils) {
		this.commonUtils = commonUtils;
	}


}
