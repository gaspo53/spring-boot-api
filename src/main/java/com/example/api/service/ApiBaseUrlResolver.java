package com.example.api.service;

import com.example.api.enums.ApiComponent;


/**
 * Interface to resolve API URL for each component (GEO, Flights, Hotels, etc),
 * since each one of these are in different locations (FML!!!).
 * @author <a href="mailto:grajoy@despegar.com">Gaspar Rajoy</a>
 *
 */
public interface ApiBaseUrlResolver {

    String getBaseUrl(ApiComponent component);
}
