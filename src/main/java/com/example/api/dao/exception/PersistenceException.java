package com.example.api.dao.exception;

/**
 * Exception for the persistence layer.<br>
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
public class PersistenceException extends RuntimeException {

	private static final long serialVersionUID = 6130671282034832051L;

	public PersistenceException() {
	
	}

	/**
	 * @param message
	 */
	public PersistenceException(String message) {
		super(message);
		
	}

	/**
	 * @param cause
	 */
	public PersistenceException(Throwable cause) {
		super(cause);
		
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	
	}

}
