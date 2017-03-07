package com.example.api.connector.strategy.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.api.common.utils.ApiCommonUtils;
import com.example.api.common.utils.LogHelper;
import com.example.api.common.utils.RequestsManager;
import com.example.api.common.utils.ResponseUtils;
import com.example.api.config.base.Profiles;
import com.example.api.connector.strategy.HttpRequestStrategy;
import com.example.api.entities.request.ApiRequest;
import com.example.api.entities.response.ApiResponse;

@Profile(value={Profiles.TESTING})
@Component("httpRequestStrategy")
public class ApiConnectorHttpRequestStrategy implements HttpRequestStrategy{

    @Autowired
    private ApiCommonUtils apiCommonUtils;
    
    @Autowired
    private CloseableHttpClient httpClient;
    
    @Autowired
    private RequestsManager requestManager;
    
    @Override
    public String request(ApiRequest apiRequest, HttpRequestBase requestMethod, ApiResponse<String> apiResponse) throws IOException {
    	String content = StringUtils.defaultString(null);
    	
    	if (this.getRequestManager().isProviderUnlocked(apiRequest)){
	    	LogHelper.info("Making request for component: "+apiRequest.getComponent()+" : "+requestMethod.getURI());

	        String requestBody = null;
	        if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(requestMethod.getClass())){
	        	HttpEntityEnclosingRequestBase enclosingRequest = (HttpEntityEnclosingRequestBase) requestMethod;
	        	HttpEntity httpEntity = enclosingRequest.getEntity();
	        	requestBody = ResponseUtils.toString(httpEntity);
		    	
	        	LogHelper.info("With body:");
	        	LogHelper.info(requestBody);
	        }
	    	
	    	
	    	CloseableHttpClient client = this.getHttpClient();
	    	HttpClientContext context = HttpClientContext.create();	    	
	    	
	    	// Makes the call
	    	HttpResponse response = client.execute(requestMethod,context);
	        HttpEntity entity = response.getEntity();
	
	        Header[] allHeaders = response.getAllHeaders();
	        if (ArrayUtils.isNotEmpty(allHeaders)){
	        	Map<String, String> apiResponseHeaders = new HashMap<String,String>();
	        	for (Header header : allHeaders){
	        		String headerName = header.getName();
	        		if (!StringUtils.equalsIgnoreCase(headerName,"Content-Length")){
	        			apiResponseHeaders.put(headerName, header.getValue());
	        		}
	        	}
	        	apiResponse.setHeaders(apiResponseHeaders);
	        }
	        
	        int statusCode = response.getStatusLine().getStatusCode();
	        
	        content = ResponseUtils.toString(entity);
	        apiResponse.setHttpStatus(statusCode);
	        apiResponse.setPlainContent(content);
	        apiResponse.setData(content);
	
	        if ( (statusCode == 403) || (statusCode == 429)){
	        	LogHelper.error(this, "ERROR: component "+apiRequest.getComponent()+" returned with HTTP Status 403, with content: "+content);
	        	this.getRequestManager().queueRequest(apiRequest, requestMethod);
	        }
	        
	        //Release resources
	        EntityUtils.consume(entity);
    	}else{
    		LogHelper.info("QUEUEING request for component: "+apiRequest.getComponent());
    		this.getRequestManager().queueRequest(apiRequest, requestMethod);
    		
	        apiResponse.setHttpStatus(403);
	        apiResponse.setPlainContent(content);
	        apiResponse.setData(content);
    	}
    	
    	this.getApiCommonUtils().setApiResponseStatus(content, apiResponse);
        
        return content;
    }

    /*
     * 
     * *** Private methods ***
     * 
     */

    //Getters and setters
    public ApiCommonUtils getApiCommonUtils() {
        return this.apiCommonUtils;
    }

    public void setApiCommonUtils(ApiCommonUtils apiCommonUtils) {
        this.apiCommonUtils = apiCommonUtils;
    }

	public CloseableHttpClient getHttpClient() {
		return this.httpClient;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public RequestsManager getRequestManager() {
		return this.requestManager;
	}

	public void setRequestManager(RequestsManager requestManager) {
		this.requestManager = requestManager;
	}
}
