package com.example.api.connector.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.UriUtils;

import com.example.api.annotation.HttpDataEntityParameter;
import com.example.api.annotation.HttpDataQueryParameter;
import com.example.api.annotation.HttpDataRequestBody;
import com.example.api.common.exception.ApiDaoException;
import com.example.api.common.utils.ApiCommonUtils;
import com.example.api.common.utils.ApiUrlBuilder;
import com.example.api.common.utils.LogHelper;
import com.example.api.common.utils.ResponseUtils;
import com.example.api.connector.ApiConnector;
import com.example.api.connector.strategy.HttpRequestStrategy;
import com.example.api.entities.PostableData;
import com.example.api.entities.request.ApiRequest;
import com.example.api.entities.response.ApiResponse;
import com.example.api.enums.ApiComponent;
import com.google.common.base.CaseFormat;

@Component
public class ApiConnectorImpl implements ApiConnector {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HttpRequestStrategy requestStrategy;
    
    @Autowired
    private ApiUrlBuilder urlBuilder;

    @Autowired
    private ApiCommonUtils apiUtils;

    private Map<ApiComponent, List<Header>> componentHeaders;
    
    @PostConstruct
    private void buildHeadersMap(){
    	this.componentHeaders = new HashMap<ApiComponent, List<Header>>();
    	for (ApiComponent ac : ApiComponent.values()){
    		List<Header> headers = new ArrayList<Header>();
    		
			String headersPropertyKey = ac.toString().concat(".api.headers");
			String headersPropertyValue = this.getApplicationContext().getEnvironment().getProperty(headersPropertyKey);
			
			if (StringUtils.isNotBlank(headersPropertyValue)){
				String[] componentHeaders = StringUtils.split(headersPropertyValue, ",");
				if (ArrayUtils.isNotEmpty(componentHeaders)){
					for (String cHeader : componentHeaders){
						String[] headerNameValue = StringUtils.split(cHeader,":");
						String name = headerNameValue[0];
						String value = headerNameValue[1];
						Header header = new BasicHeader(name, value);
						headers.add(header);
					}
				}
			}

			this.componentHeaders.put(ac, headers);
    	}
    }

    @Override
    public ResponseEntity<String> get(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;
        try {
            String url = apiRequest.getUrl();
            HttpGet method = new HttpGet(url);

            apiResponse = this.apiCall(apiRequest, method);
        }catch (Exception e) {
            // TODO should create ApiRequest with status error default
            LogHelper.error(this, e);
        }
        return apiResponse;
    }

    @Override
    public ResponseEntity<String> post(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;

        try {
            HttpPost method = new HttpPost(apiRequest.getUrl());
            String requestBody = this.createRequestBody(apiRequest,method);
            apiRequest.setEntity(requestBody);
            apiResponse = this.apiCall(apiRequest, method);
        } catch (Exception e) {
            // FIXME what to do here?
            LogHelper.error(this, e);
        }

        return apiResponse;
    }

    @Override
    public ResponseEntity<String> patch(ApiRequest apiRequest) throws ApiDaoException {
        ResponseEntity<String> apiResponse = null;

        try {
            HttpPatch method = new HttpPatch(apiRequest.getUrl());
            String requestBody = this.createRequestBody(apiRequest,method);
            apiRequest.setEntity(requestBody);

            apiResponse = this.apiCall(apiRequest, method);
        } catch (Exception e) {
            LogHelper.error(this, e);
        }

        return apiResponse;
    }
    
    @Override
    public ResponseEntity<String> put(ApiRequest apiRequest) throws ApiDaoException {
    	ResponseEntity<String> apiResponse = null;
    	
    	try {
    		HttpPut method = new HttpPut(apiRequest.getUrl());
    		String requestBody = this.createRequestBody(apiRequest,method);
    		apiRequest.setEntity(requestBody);
    		
    		apiResponse = this.apiCall(apiRequest, method);
    	} catch (Exception e) {
    		LogHelper.error(this, e);
    	}
    	
    	return apiResponse;
    }

    // TODO remainig methods

    @Override
    public ResponseEntity<String> head(ApiRequest apiRequest) throws ApiDaoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<String> delete(ApiRequest apiRequest) throws ApiDaoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<String> options(ApiRequest apiRequest) throws ApiDaoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<String> apiCall(ApiRequest apiRequest, HttpRequestBase method) {
    	ResponseEntity<String> responseEntity = null;
    	
    	if (apiRequest.getComponent() == null)  {
    		throw new RuntimeException("Null component in ApiRequest. Cannot continue");
    	}
    	
    	try {
    		//If ApiRequest is queued, it means that already has all the data
    		if (!apiRequest.isQueued()){
    			this.getApiUtils().setApiProperties(apiRequest);
    			this.addPathValues(apiRequest, method);
    			this.addAdditionalQueryParams(apiRequest, method);
    			
    			apiRequest.setUrlWithParams(method.getURI().toString());
    			this.addAdditionalHeaders(apiRequest, method);
    		}
    		
    		ApiResponse<String> apiResponse = new ApiResponse<>();
    		String content = this.getRequestStrategy().request(apiRequest, method, apiResponse);
    		
    		HttpHeaders httpHeaders = new HttpHeaders();
    		Map<String, String> apiResponseHeaders = apiResponse.getHeaders();
    		if (MapUtils.isNotEmpty(apiResponseHeaders)){
    			for (Entry<String, String> entry : apiResponseHeaders.entrySet()) {
    				httpHeaders.set(entry.getKey(), entry.getValue());
    			}
    		}
    		responseEntity = ResponseEntity.status(apiResponse.getHttpStatus()).headers(httpHeaders).body(content);
    		
    	} catch (Exception e) {
    		LogHelper.error(this, e);
    	}
    	return responseEntity;
    }
    

    /*
     * *******************************************************
     * *******************************************************
     * ****************** Private methods ********************
     * *******************************************************
     * *******************************************************
     */

	/**
     * Adds the headers in {@link ApiRequest#getHeaders()} to the current call
     * @param apiRequest
     * @param method
     */
    private void addPathValues(ApiRequest apiRequest, HttpRequestBase method) {
        URI originalMethodUri = method.getURI();
        try {
            if (apiRequest.hasPathValues()) {
                String uriString = originalMethodUri.toString();
                uriString = this.getUrlBuilder().addPathValuesToUrl(apiRequest, uriString);
                URI uri = new URI(uriString);
                method.setURI(uri);
            }
        } catch (Exception e) {
            LogHelper.error(this, e);
            method.setURI(originalMethodUri);
        }

    }

    /**
     * Adds the headers in {@link ApiRequest#getHeaders()} to the current call
     * @param apiRequest
     * @param method
     */
    private void addAdditionalHeaders(ApiRequest apiRequest, HttpRequestBase method) {
    	try{
    		ApiComponent component = apiRequest.getComponent();
    		List<Header> customHeaders = this.componentHeaders.get(component);
    		apiRequest.getHeaders().addAll(customHeaders);
	        Iterator<Header> iterator = apiRequest.getHeaders().iterator();
	        while (iterator.hasNext()) {
	            Header header = iterator.next();
	            if (ArrayUtils.isEmpty(method.getHeaders(header.getName()))) {
	                method.addHeader(header);
	            }
	        }
    	}catch(Exception e){
    		LogHelper.error(this,"Error adding headers to the ApiRequest", e);
    	}
    }

    /**
     * Adds the parameters in {@link ApiRequest#getQueryParameters()} to the current call.<br>
     * Parameters with same name, are joined and splitted by a comma. I.e: https://host.com?test=val1,val2,val3&param2=3
     * @param apiRequest
     * @param method
     */
    private void addAdditionalQueryParams(ApiRequest apiRequest, HttpRequestBase method) {
        URI originalMethodUri = method.getURI();
        try {
            this.resolveHttpQueryParameters(apiRequest);
            if (apiRequest.hasAdditionalQueryParams()) {
                String decodedURL = this.getUrlBuilder().addParameters(apiRequest, originalMethodUri);
                URI methodUri = URI.create(UriUtils.encodeQuery(decodedURL,"UTF-8"));
                method.setURI(methodUri);
            }
        } catch (Exception e) {
            LogHelper.error(this, e);
            method.setURI(originalMethodUri);
        }
    }

    /**
     * Scans the {@link ApiRequest#getRequestData()} to retrieve each field annotated with {@link HttpDataRequestBody}.<br>
     * <b> Warning: only takes the first <b>NOT NULL</b> occurrence!!!!!!!!!!!!!!!!!! </b>
     * @param apiRequest
     * @return
     */
    private String createRequestBody(ApiRequest apiRequest, HttpEntityEnclosingRequest request) {
        String requestBody = null;
        if (apiRequest.isFormDataRequest()){
        	this.createFormDataEntity(apiRequest,request);
        }else{
        	this.createJsonRequestBody(apiRequest,request);
        	
        }

        return requestBody;
    }

	private void createJsonRequestBody(ApiRequest apiRequest, HttpEntityEnclosingRequest method) {
		try {
        	PostableData requestData = apiRequest.getRequestData();
            HttpDataRequestBody annotation = null;
            if (requestData != null) {
                BeanInfo beanInfo = Introspector.getBeanInfo(requestData.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                int fieldsCant = propertyDescriptors.length;
                boolean converted = false;
                for (int index = 0; ((index < fieldsCant) && (!converted)); index++) {
                    PropertyDescriptor descriptor = propertyDescriptors[index];
                    if (descriptor.getReadMethod() != null) {
                        if (descriptor.getReadMethod().isAnnotationPresent(HttpDataRequestBody.class)) {
                            annotation = descriptor.getReadMethod().getAnnotation(HttpDataRequestBody.class);
                            if (annotation != null) {
                                Object objectToConvert = descriptor.getReadMethod().invoke(requestData, new Object[0]);
                                converted = (objectToConvert != null);
                                if (converted){
                                	String requestBody = null;
                                    if (annotation.asProperty()){
                                        String fieldName = WordUtils.capitalize(descriptor.getName());
                                        fieldName = CaseFormat.UPPER_CAMEL.to(annotation.format(), fieldName);
                                        
                                        Map<String, Object> jsonObject = new HashMap<String, Object>();
                                        jsonObject.put(fieldName, objectToConvert);
                                        
                                        requestBody = ResponseUtils.createJsonString(jsonObject, apiRequest.getResponseFormat());
                                    }else{
                                        requestBody = ResponseUtils.createJsonString(objectToConvert, apiRequest.getResponseFormat());
                                    }
                                    
                                    apiRequest.setEntity(requestBody);
                                    if (StringUtils.isNotEmpty(requestBody)) {
                                        StringEntity entity = new StringEntity(requestBody, Charset.forName("UTF-8").toString());
                                        entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                        method.setEntity(entity);
                                    }                                    
                                    
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.info(this, e);
        }
	}


    /**
     * Scans the {@link ApiRequest#getRequestData()} to retrieve each field annotated with {@link HttpDataQueryParameter}
     * @param apiRequest
     * @param request 
     */
    @SuppressWarnings("unchecked")
	private void createFormDataEntity(ApiRequest apiRequest, HttpEntityEnclosingRequest method) {
        try {
        	PostableData requestData = apiRequest.getRequestData();
            if (requestData != null) {
            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            	
                BeanInfo beanInfo = Introspector.getBeanInfo(requestData.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor descriptor : propertyDescriptors) {
                    if (descriptor.getReadMethod() != null) {
                        if (descriptor.getReadMethod().isAnnotationPresent(HttpDataEntityParameter.class)) {
                            String fieldName = descriptor.getName();
                            Object fieldValue = descriptor.getReadMethod().invoke(requestData, new Object[0]);
                            HttpDataEntityParameter annotation = descriptor.getReadMethod().getAnnotation(HttpDataEntityParameter.class);
                            
                            if ((fieldValue != null) && (annotation.commaDelimited())){
	                            boolean isCollection = ClassUtils.isAssignable(Collection.class, fieldValue.getClass());
	                            if (isCollection){
	                            	Collection<String> collection = (Collection<String>) fieldValue;
	                            	if (!collection.isEmpty()){
	                            		fieldValue = org.springframework.util.StringUtils.collectionToCommaDelimitedString(collection);
	                            	}
	                            }
                            }
                            if (!annotation.asDeclared()){
	                            if (StringUtils.isNotEmpty(annotation.name())) {
	                                fieldName = annotation.name();
	                            } else {
	                                fieldName = WordUtils.capitalize(fieldName);
                            		fieldName = CaseFormat.UPPER_CAMEL.to(annotation.format(), fieldName);
	                            }
                            }
                            String stringFieldValue = Objects.toString(fieldValue,"");
                            if (StringUtils.isNotBlank(stringFieldValue)){
                            	nameValuePairs.add(new BasicNameValuePair(fieldName, stringFieldValue));
                            }
                        }
                    }
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
                
                apiRequest.setEntity(formEntity);
                method.setEntity(formEntity);
            }
        } catch (Exception e) {
            LogHelper.info(this, e);
        }
    }

    /**
     * Scans the {@link ApiRequest#getRequestData()} to retrieve each field annotated with {@link HttpDataQueryParameter}
     * @param apiRequest
     */
    @SuppressWarnings("unchecked")
	private void resolveHttpQueryParameters(ApiRequest apiRequest) {
        try {
        	PostableData requestData = apiRequest.getRequestData();
            if (requestData != null) {
                BeanInfo beanInfo = Introspector.getBeanInfo(requestData.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor descriptor : propertyDescriptors) {
                    if (descriptor.getReadMethod() != null) {
                        if (descriptor.getReadMethod().isAnnotationPresent(HttpDataQueryParameter.class)) {
                            String fieldName = descriptor.getName();
                            Object fieldValue = descriptor.getReadMethod().invoke(requestData, new Object[0]);
                            HttpDataQueryParameter annotation = descriptor.getReadMethod().getAnnotation(HttpDataQueryParameter.class);
                            
                            if ((fieldValue != null) && (annotation.commaDelimited())){
	                            boolean isCollection = ClassUtils.isAssignable(Collection.class, fieldValue.getClass());
	                            if (isCollection){
	                            	fieldValue = org.springframework.util.StringUtils.collectionToCommaDelimitedString((Collection<String>) fieldValue);
	                            }
                            }

                            if (!annotation.asDeclared()){
	                            if (StringUtils.isNotEmpty(annotation.name())) {
	                                fieldName = annotation.name();
	                            } else {
	                                fieldName = WordUtils.capitalize(fieldName);
                            		fieldName = CaseFormat.UPPER_CAMEL.to(annotation.format(), fieldName);
	                            }
                            }
                            apiRequest.addQueryParameter(fieldName, fieldValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.info(this, e);
        }
    }

    // Getters and setters
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

    public HttpRequestStrategy getRequestStrategy() {
        return this.requestStrategy;
    }

    public void setRequestStrategy(HttpRequestStrategy requestStrategy) {
        this.requestStrategy = requestStrategy;
    }
}
