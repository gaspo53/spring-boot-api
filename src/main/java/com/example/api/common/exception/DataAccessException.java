package com.example.api.common.exception;

/**
 * This exception, and all subclasses, has to be used in the Data acces layer (DAO)
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
public class DataAccessException extends Exception{

	private static final long serialVersionUID = 5289447833720832409L;
	
	//Default constructor
	public DataAccessException(){}
	
	public DataAccessException(String message){
		super(message);
	}
}
