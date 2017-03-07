package com.example.api.service;

import org.springframework.http.ResponseEntity;

import com.example.api.common.exception.ApiServiceException;
import com.example.api.entities.request.ApiRequest;

public interface BaseApiService {

    ResponseEntity<String> get(ApiRequest apiRequest) throws ApiServiceException;

    ResponseEntity<String> head(ApiRequest apiRequest) throws ApiServiceException;

    ResponseEntity<String> post(ApiRequest apiRequest) throws ApiServiceException;

    ResponseEntity<String> patch(ApiRequest apiRequest) throws ApiServiceException;

    ResponseEntity<String> put(ApiRequest apiRequest) throws ApiServiceException;

    ResponseEntity<String> delete(ApiRequest apiRequest) throws ApiServiceException;

    ResponseEntity<String> options(ApiRequest apiRequest) throws ApiServiceException;

    <T> ResponseEntity<T> get(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException;

    <T> ResponseEntity<T> head(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException;

    <T> ResponseEntity<T> post(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException;

    <T> ResponseEntity<T> patch(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException;

    <T> ResponseEntity<T> put(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException;

    <T> ResponseEntity<T> delete(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException;

    <T> ResponseEntity<T> options(ApiRequest apiRequest, Class<T> responseType) throws ApiServiceException;

    String getApiKey();
    
    boolean isCacheable();
    
    int cacheTtl();

}
