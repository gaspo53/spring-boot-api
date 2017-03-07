package com.example.api.entities;

import java.io.Serializable;

import org.apache.http.client.methods.HttpRequestBase;

import com.example.api.entities.request.ApiRequest;

public class ApiCall implements Serializable{

	private static final long serialVersionUID = -1957136726276871524L;

	private ApiRequest apiRequest;
	
	private HttpRequestBase requestMethod;

	public ApiCall() {}
	
	public ApiCall(ApiRequest apiRequest, HttpRequestBase requestMethod){
		this.apiRequest = apiRequest;
		this.requestMethod = requestMethod;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ApiCall [");
		if (this.apiRequest != null) {
			builder.append("apiRequest=");
			builder.append(this.apiRequest);
			builder.append(", ");
		}
		if (this.requestMethod != null) {
			builder.append("requestMethod=");
			builder.append(this.requestMethod);
		}
		builder.append("]");
		return builder.toString();
	}

	//Getters and setters
	public ApiRequest getApiRequest() {
		return this.apiRequest;
	}

	public void setApiRequest(ApiRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	public HttpRequestBase getRequestMethod() {
		return this.requestMethod;
	}

	public void setRequestMethod(HttpRequestBase requestMethod) {
		this.requestMethod = requestMethod;
	}
}
