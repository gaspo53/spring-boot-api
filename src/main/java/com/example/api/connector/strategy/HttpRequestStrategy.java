package com.example.api.connector.strategy;

import java.io.IOException;

import org.apache.http.client.methods.HttpRequestBase;

import com.example.api.entities.request.ApiRequest;
import com.example.api.entities.response.ApiResponse;

/**
 * Makes the Request, with the received method, and sets that content into the {@link ApiResponse}.
 * @author <a href="mailto:grajoy@despegar.com">Gaspar Rajoy</a>
 *
 */
public interface HttpRequestStrategy {

    String request(ApiRequest apiRequest, HttpRequestBase requestMethod, ApiResponse<String> apiResponse) throws IOException;
    
}
