package com.example.api.entities.response;

import java.util.Map;

import org.apache.http.HttpStatus;

import com.example.api.entities.request.ApiRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Maps a response from an API.
 *
 */
public abstract class ApiResponseBase{

    /**
     * The initiator request of this response.
     */
    @JsonIgnore
    private ApiRequest apiRequest;
    
	/**
	 * The HTTP status code.
	 * @see {@link HttpStatus} for the complete list.
	 */
	private int httpStatus;
	
	/**
	 * The response from API.
	 */
	@JsonIgnore
	private String plainContent;
	
	private ApiResponseStatus apiStatus;
	
	/**
	 * Response headers (if any)
	 */
	@JsonIgnore
	private Map<String,String> headers;
	
    /**
     * Tells if this response was obtained from cache.
     */
    private Boolean cached;
    
	/**
	 * Returns the response as a {@link String}
	 * @return
	 */
	@JsonIgnore
	public String getPlainResponseContent(){
	    return this.plainContent;
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ApiResponseBase [");
        if (this.apiRequest != null) {
            builder.append("apiRequest=");
            builder.append(this.apiRequest);
            builder.append(", ");
        }
        builder.append("httpStatus=");
        builder.append(this.httpStatus);
        builder.append(", ");
        if (this.plainContent != null) {
            builder.append("plainContent=");
            builder.append(this.plainContent);
            builder.append(", ");
        }
        if (this.apiStatus != null) {
            builder.append("apiStatus=");
            builder.append(this.apiStatus);
            builder.append(", ");
        }
        if (this.cached != null) {
            builder.append("cached=");
            builder.append(this.cached);
        }
        if (this.headers != null) {
            builder.append("headers=");
            builder.append(this.headers);
        }
        builder.append("]");
        return builder.toString();
    }



    //Getters and setters
	public int getHttpStatus() {
		return this.httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public void setPlainContent(String response) {
		this.plainContent = response;
	}


    public ApiResponseStatus getApiStatus() {
        return this.apiStatus;
    }


    public void setApiStatus(ApiResponseStatus apiStatus) {
        this.apiStatus = apiStatus;
    }


    public ApiRequest getApiRequest() {
        return this.apiRequest;
    }


    public void setApiRequest(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }


    public Boolean isCached() {
        return this.cached;
    }


    public void setCached(Boolean cached) {
        this.cached = cached;
    }

	public Map<String,String> getHeaders() {
		return this.headers;
	}

	public void setHeaders(Map<String,String> headers) {
		this.headers = headers;
	}
	
}
