package com.example.api.config.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClientOptions;

@Configuration
public class MongoConfiguration{

	@Bean
    public MongoClientOptions mongoOptionsConfigurer(){
    	MongoClientOptions options = MongoClientOptions.builder()
        .connectionsPerHost(250)
        .build();    	
    	
    	return options;
    }
    
	
}
