package com.example.api.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.api.common.utils.LogHelper;
import com.example.api.enums.ApiComponent;
import com.example.api.service.ApiKeyResolverService;

/**
 * Default implementation of {@link ApiKeyResolverService}.<br>
 *
 */
@Service
public class ApiKeyResolverServiceImpl implements ApiKeyResolverService {

	@Autowired
	private Environment environment;
	
    @Override
    public String getApiKey(ApiComponent component) {
    	String apiKey = null;
    	String randomKey = null;
    	try{
    		String propertyKey = component.toString();
    		propertyKey = propertyKey.concat(".api.key");
    		
    		apiKey = this.getEnvironment().getProperty(propertyKey);
    		
			String[] apiKeyList = apiKey.split(",");
    		int idx = new Random().nextInt(apiKeyList.length);
    		randomKey = (apiKeyList[idx]);
    		apiKey = randomKey;
 		
    	}catch(Exception e){
    		LogHelper.error(this, "ApiKey for component ".concat(component.toString()).concat(" NOT FOUND"));
    		
    	}
    	
    	return apiKey;
    }

    
    //Getters and setters
	public Environment getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
