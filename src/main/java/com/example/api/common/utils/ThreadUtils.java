package com.example.api.common.utils;

import java.util.concurrent.Future;

import org.springframework.stereotype.Component;

/**
 * Simple util class to perform operations with Threads, and related stuff.<br>
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
@Component
public class ThreadUtils {
	
	/**
	 * Calls get() to the {@link Future} object
	 * @param future
	 * @param type
	 * @return the type of the Future object, NULL on {@link Exception}
	 */
	public <T> T getAsyncResult(Future<T> future, Class<T> type){
		T result = null;
		try{
			result = future.get();
			return result;
		}catch (Exception e){
			LogHelper.warn(this,e);
		}
		
		return result;
	}

}
