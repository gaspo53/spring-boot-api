package com.example.api.entities.request;

import com.example.api.entities.PostableData;
import com.example.api.enums.ApiComponent;



/**
 * Entity used to make ApiV3 calls. DAO's and Connectors make use of this.<br>
 * To preserve conventions, do NOT make interfaces receiving the real parameters, use this as parameter instead.
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
public class ApiRequest extends BaseApiRequest {

    private static final long serialVersionUID = -4462266727326980898L;

    public static final String PRODUCT_KEY = "product";
    public static final String COUNTRY_ID_KEY = "countryId";
    public static final String COUNTRY_KEY = "country";
    public static final String SITE_KEY = "site";
    public static final String CHANNEL_KEY = "channel";
    public static final String HEADER_API_KEY = "X-ApiKey";
    public static final String OFFSET_KEY = "offset";
    public static final String LIMIT_KEY = "limit";
    public static final String SNAPSHOT_KEY = "snapshot";

    private String url;
    private String urlWithParams;
    private Object entity;

    // Default constructor
    public ApiRequest() {}

    public ApiRequest(ApiComponent component, String countryId, String url) {
        this();
        this.setComponent(component);
        this.setCountry(countryId);
        this.setUrl(url);
    }

    public ApiRequest(BaseApiRequest baseRequest, String url) {
        this(baseRequest.getComponent(), baseRequest.getCountry(), url);
        
        this.setResponseFormat(baseRequest.getResponseFormat());
        this.setCurrentLocale(baseRequest.getCurrentLocale());
        this.setProduct(baseRequest.getProduct());
        this.setOffset(baseRequest.getOffset());
        this.setLimit(baseRequest.getLimit());
        this.setHeaders(baseRequest.getHeaders());
        this.setQueryParameters(baseRequest.getQueryParameters());
        this.setPathValues(baseRequest.getPathValues());
        this.setCacheable(baseRequest.isCacheable());
        this.setCacheTtl(baseRequest.getCacheTtl());
        this.setCountry(baseRequest.getCountry());
        this.setComponent(baseRequest.getComponent());
        this.setFormDataRequest(baseRequest.isFormDataRequest());
        this.setApiContext(baseRequest.isApiContext());
        
        this.setUsedApiKey(baseRequest.getUsedApiKey());
        
        this.setForcedRequest(baseRequest.isForcedRequest());
        this.setQueued(baseRequest.isQueued());
        
        this.setPathValueDelimiter(baseRequest.getPathValueDelimiter());
        this.setPathValuesOpenString(baseRequest.getPathValuesOpenString());
        this.setPathValuesCloseString(baseRequest.getPathValuesCloseString());
    }
    
    public ApiRequest(BaseApiRequest baseRequest, String url, ApiComponent component) {
        this(baseRequest,url);
        this.setComponent(component);
    }

    public ApiRequest(String url, ApiComponent component) {
    	this();
        this.setUrl(url);
        this.setComponent(component);
    }
    
    public ApiRequest(BaseApiRequest baseRequest, String finalURL, ApiComponent component, PostableData requestData) {
        this(baseRequest, finalURL, component);
        this.setRequestData(requestData);
    }


    // Getters and setters
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public String getUrlWithParams() {
		return this.urlWithParams;
	}

	public void setUrlWithParams(String urlWithParams) {
		this.urlWithParams = urlWithParams;
	}

	public Object getEntity() {
		return this.entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

}
