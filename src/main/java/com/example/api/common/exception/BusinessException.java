package com.example.api.common.exception;

/**
 * This exception, and all subclasses, has to be used in the Business layer (services)
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
public class BusinessException extends Exception{

	private static final long serialVersionUID = 7737262849670518086L;
	
	//Default constructor
	public BusinessException(){}
	
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}
