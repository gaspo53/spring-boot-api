package com.example.api.config.base;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.cache.CacheBuilder;

/**
 * Annotated web-config.<br>
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private Environment env;

	
	@Override
	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
		
		GuavaCache geoPointCache = new GuavaCache("geoPointCache", CacheBuilder.newBuilder()
																  .expireAfterAccess(30, TimeUnit.DAYS)
																  .build());

		GuavaCache generalCache = new GuavaCache("generalCache", CacheBuilder.newBuilder()
																  .expireAfterAccess(1, TimeUnit.DAYS)
																  .build());

		GuavaCache locationCache = new GuavaCache("locationCache", CacheBuilder.newBuilder()
																  .expireAfterAccess(30, TimeUnit.DAYS)
																  .build());

		GuavaCache routeItineraryCache = new GuavaCache("routeItineraryCache", CacheBuilder.newBuilder()
																			  .expireAfterAccess(48, TimeUnit.HOURS)
																			  .build());

		GuavaCache booleanCache = new GuavaCache("booleanCache", CacheBuilder.newBuilder()
																  .expireAfterAccess(30, TimeUnit.DAYS)
																  .build());

		GuavaCache integerCache = new GuavaCache("integerCache", CacheBuilder.newBuilder()
																  .expireAfterAccess(30, TimeUnit.DAYS)
																  .build());

		
		simpleCacheManager.setCaches(Arrays.asList(geoPointCache,generalCache,locationCache,routeItineraryCache,booleanCache,integerCache));
		
		return simpleCacheManager;
	}

	// Getters and Setters
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
