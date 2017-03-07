package com.example.api.entities.response;

import java.util.List;

/**
 * Represents the Status code and message of Api errors.<br>
 * i.e: {"status":400,"message":"Request is missing required query parameter 'site'","causes":[]}
 * 
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
public class ApiResponseStatus {
    
    private int status;
    private String message;
    private String code;
    private List<String> causes;
    
    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ApiResponseStatus [status=");
		builder.append(this.status);
		builder.append(", ");
		if (this.message != null) {
			builder.append("message=");
			builder.append(this.message);
			builder.append(", ");
		}
		if (this.code != null) {
			builder.append("code=");
			builder.append(this.code);
			builder.append(", ");
		}
		if (this.causes != null) {
			builder.append("causes=");
			builder.append(this.causes);
		}
		builder.append("]");
		return builder.toString();
	}
    
	//Getters and setters
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<String> getCauses() {
        return this.causes;
    }
    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
    

}
