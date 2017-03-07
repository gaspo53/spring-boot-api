/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.common.utils.CommonUtils;
import com.example.api.enums.ApiComponent;
import com.example.api.service.ApiComponentService;

/**
 * @author gaspar
 *
 */
@Service
public class ApiComponentServiceImpl implements ApiComponentService{

	@Autowired
	private CommonUtils commonUtils;
	
	
	@Override
	public boolean isEnabled(ApiComponent apiComponent) {
		String propertyKey = apiComponent.toString().concat(".enabled");
		Boolean propertyValue = this.getCommonUtils().getProperty(propertyKey, Boolean.class);
		
		return BooleanUtils.isTrue(propertyValue);
	}

	//Getters and setters
	public CommonUtils getCommonUtils() {
		return this.commonUtils;
	}

	public void setCommonUtils(CommonUtils commonUtils) {
		this.commonUtils = commonUtils;
	}
}
