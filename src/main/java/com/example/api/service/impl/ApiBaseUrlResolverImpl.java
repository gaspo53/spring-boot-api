package com.example.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.api.enums.ApiComponent;
import com.example.api.service.ApiBaseUrlResolver;

@Service
public class ApiBaseUrlResolverImpl implements ApiBaseUrlResolver{
    
	@Autowired
	private Environment environment;
	
    @Override
    public String getBaseUrl(ApiComponent component) {
        String baseUrl = StringUtils.defaultString(null);
        String posfixProperty = ".api.url";
        String finalProperty = component.toString().concat(posfixProperty);
        
        baseUrl = this.getEnvironment().getProperty(finalProperty);
        
        return baseUrl;
    }

    
    //Getters and setters
	public Environment getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
