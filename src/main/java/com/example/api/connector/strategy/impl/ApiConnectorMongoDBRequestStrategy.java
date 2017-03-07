package com.example.api.connector.strategy.impl;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.api.common.utils.LogHelper;
import com.example.api.common.utils.ResponseUtils;
import com.example.api.config.base.Profiles;
import com.example.api.entities.CacheUrlData;
import com.example.api.entities.request.ApiRequest;
import com.example.api.entities.response.ApiResponse;
import com.example.api.service.CacheDataService;

/**
 * Extends to {@link ApiConnectorHttpRequestStrategy} to handle the request, but first asks to Mongo if has it.<br>
 * This class is also responsible of mantaining the cache.
 * @author <a href="mailto:grajoy@despegar.com">Gaspar Rajoy</a>
 *
 */
@Profile(value={Profiles.DEVELOP,Profiles.PRODUCTION})
@Primary
@Component("cacheRequestStrategy")
public class ApiConnectorMongoDBRequestStrategy extends ApiConnectorHttpRequestStrategy{

    @Autowired
    private CacheDataService cacheDataService;
    
    @Override
    public String request(ApiRequest apiRequest, HttpRequestBase requestMethod, ApiResponse<String> apiResponse) throws IOException {
        String content = null;
        String url = Objects.toString(requestMethod.getURI(),"");
        
        String hashValue = StringUtils.defaultString(url);

        String requestBody = null;
        if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(requestMethod.getClass())){
        	HttpEntityEnclosingRequestBase enclosingRequest = (HttpEntityEnclosingRequestBase) requestMethod;
        	HttpEntity httpEntity = enclosingRequest.getEntity();
        	requestBody = ResponseUtils.toString(httpEntity);
        	hashValue = hashValue.concat(requestBody);
        }
        
        hashValue = DigestUtils.sha256Hex(hashValue);
        
        boolean cacheable = apiRequest.isCacheable();
        
        try{
	        if (cacheable){
	            content = this.getCacheDataService().get(hashValue);
	            apiResponse.setCached(true);
	            apiResponse.setHttpStatus(HttpStatus.OK.value());
	        }
        }catch(Exception e){
        	LogHelper.error(this,e);
        	LogHelper.error(this, "MongoDB is Down!!!!!!!!!!!");
        }
        
        if (apiRequest.isForcedRequest()){
        	content = StringUtils.defaultString(null);
        	this.getCacheDataService().delete(hashValue);
        }
        
        if (StringUtils.isBlank(content)){
            content = super.request(apiRequest, requestMethod, apiResponse);
            apiResponse.setCached(false);
            if (apiResponse.isSuccessResponse()){
            	apiResponse.setData(content);
	            if ( (cacheable) && (apiResponse.hasData()) ){
	                try{
	                	CacheUrlData cacheData = new CacheUrlData();
	                    cacheData.setKey(hashValue);
	                    cacheData.setUrl(url);
	                    cacheData.setValue(content);
	                    cacheData.setRequestBody(requestBody);
	                    Integer seconds = apiRequest.getCacheTtl();
	                    
	                    String cacheControl = apiResponse.getHeaders().get("Cache-Control");
                		boolean isHeaderCacheable = StringUtils.isNotBlank(cacheControl) && StringUtils.contains(cacheControl, "max-age");
                		if (isHeaderCacheable){
                			String maxAge = cacheControl.replaceAll("\\D+","");
                			
                			int headerSeconds = NumberUtils.toInt(maxAge);
                			if (headerSeconds > 0){
                				seconds = headerSeconds;
                			}
                		}
						cacheData.expiresAfter(seconds);
	                    this.getCacheDataService().save(cacheData);
	                }catch(Exception e){
	                    LogHelper.error(this,e);
	                    LogHelper.error(this,"Error saving CacheData for url: " + url);
	                }
	            }
            }
        }else{
        	LogHelper.info("Success CACHED request for component: "+apiRequest.getComponent());
        }
        apiResponse.setPlainContent(content);
        
        return content;
    }

    /*
     * *** Private methods ***
     */
    

    //Getters and setters
    public CacheDataService getCacheDataService() {
        return this.cacheDataService;
    }

    public void setCacheDataService(CacheDataService cacheDataService) {
        this.cacheDataService = cacheDataService;
    }
    

}
