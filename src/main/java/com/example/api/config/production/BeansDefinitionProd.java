package com.example.api.config.production;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import com.example.api.config.base.Profiles;

/**
 * Bean definitions for the application.<br>
 * Here are defined beans that require initialisation.
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
@Profile(Profiles.PRODUCTION)
@Configuration
public class BeansDefinitionProd {
	
	@Autowired
	private Environment env;
    
	@Autowired
	private ApplicationContext applicationContext;
	
    
    //Getters and setters 
    public Environment getEnv() {
		return this.env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
}
