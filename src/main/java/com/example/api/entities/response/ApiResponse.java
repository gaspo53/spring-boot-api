package com.example.api.entities.response;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;



/**
 * Maps a response from ApiV3.
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 * 
 *
 */
public final class ApiResponse<T> extends ApiResponseBase {

	/**
	 * The parsed content of the {@link #httpEntity}
	 */
	private T data;

	//Default constructor
    public ApiResponse() {}
	
    /** Creates an instance based on another {@link ApiResponseBase}
     * @param apiResponse
     */
    public ApiResponse(ApiResponseBase apiResponse) {
        if (apiResponse != null){
            this.setHttpStatus(apiResponse.getHttpStatus());
            this.setPlainContent(apiResponse.getPlainResponseContent());
            this.setApiStatus(apiResponse.getApiStatus());
            this.setCached(apiResponse.isCached());
            this.setApiRequest(apiResponse.getApiRequest());
            this.setHeaders(apiResponse.getHeaders());
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ApiResponse [");
        if (this.data != null) {
            builder.append("data=");
            builder.append(this.data);
            builder.append(", ");
        }
        if (this.isCached() != null) {
            builder.append("cached=");
            builder.append(this.isCached());
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Determines if the response has any data
     * @return
     */
    public boolean hasData(){
        boolean hasData = (this.getData() != null);
        
        return hasData;
    }
    
    /**
     * Tells if the response is a {@link HttpStatus#SC_OK}
     * @param apiResponse
     * @return
     */
    @JsonIgnore
    public boolean isSuccessResponse() {
        int statusCode = this.getHttpStatus();
        boolean isValidResponse = ( (statusCode >= HttpStatus.SC_OK) && (statusCode < HttpStatus.SC_MULTIPLE_CHOICES) );

        return isValidResponse;
    }
    
    //Getters and setters
    public T getData() {
        return this.data;
    }
    
	public void setData(T content) {
        this.data = content;
    }

}
