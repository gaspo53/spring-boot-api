package com.example.api.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Singleton pattern.<br>
 * Simple Utils class to help with application logging.<br>
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
public class LogHelper{
	
	//To prevent instantiation
	private LogHelper(){}
	
	public static void error(Class<? extends Object> clazz, String string){
		Logger logger = LogManager.getLogger(clazz);
		String stringToLog = string;
		try{
			logger.error(stringToLog);
		}catch (Exception e){
			logger.error("LogHelper --- " + e.getMessage());
		}		
	}

	public static void error(Class<? extends Object> clazz, String string, Throwable exception){
		Logger logger = LogManager.getLogger(clazz);
		String stringToLog = string;
		try{
			logger.error(stringToLog);
			logger.error(exception);
		}catch (Exception e){
			logger.error("LogHelper --- " + e.getMessage());
		}		
	}

	public static void error(Object object, String string, Throwable exception){
			error(object.getClass(),string,exception);
	}

	public static void error(Object object, String string){
		String stringToLog = string;
		try{
			error(object.getClass(), stringToLog);
		}catch (Exception e){
			error("LogHelper --- ", stringToLog);
		}
	}
	
	public static void error(Class<? extends Object> clazz, Exception exception){
		
		try{
			Logger logger = LogManager.getLogger(clazz);
			logger.error(exception);
		}catch (Exception e){
			error("LogHelper --- ", e.getMessage());
		}		
	}

	public static void error(Object object, Exception exception){
		try{
			error(object.getClass(), exception);
			exception.printStackTrace();
		}catch (Exception e){
			error("LogHelper --- ", e.getMessage());
		}
		
	}

	public static void info(Class<? extends Object> clazz, String string){
		Logger logger = LogManager.getLogger(clazz);
		logger.info(string);
	}

	public static void info(Object object, String string){
		info(object.getClass(), string);
	}

	public static void warn(Class<? extends Object> clazz, String string){
		Logger logger = LogManager.getLogger(clazz);
		logger.warn(string);
	}

	public static void warn(Object object, String string){
		warn(object.getClass(), string);
	}

	public static void debug(Class<? extends Object> clazz, String string){
		Logger logger = LogManager.getLogger(clazz);
		logger.debug(string);
	}

	public static void debug(Object object, String string){
		debug(object.getClass(), string);
	}
	
	public static void debug(String string){
		Logger logger = LogManager.getLogger("default");
		logger.debug(string);
	}
	
	public static void info(String string){
		Logger logger = LogManager.getLogger("default");
		logger.info(string);
	}

	public static void warn(String string){
		Logger logger = LogManager.getLogger("default");
		logger.warn(string);
	}
	

	/*
	 * 
	 * Methods receiving Exception directly (avoid discarding exception)
	 * 
	 */
	public static void info(Class<? extends Object> clazz, Exception exception){
		Logger logger = LogManager.getLogger(clazz);
		logger.info(exception.getMessage());
	}

	public static void info(Object object, Exception exception){
		info(object.getClass(), exception.getMessage());
	}

	public static void warn(Class<? extends Object> clazz, Exception exception){
		Logger logger = LogManager.getLogger(clazz);
		logger.warn(exception.getMessage());
	}

	public static void warn(Object object, Exception exception){
		warn(object.getClass(), exception.getMessage());
	}

	public static void debug(Class<? extends Object> clazz, Exception exception){
		Logger logger = LogManager.getLogger(clazz);
		logger.debug(exception.getMessage());
	}

	public static void debug(Object object, Exception exception){
		debug(object.getClass(), exception.getMessage());
	}
	
}
