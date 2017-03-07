package com.example.api.common.exception;


/**
 * Base {@link Exception} class to model APIv3 call errors.
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
public class ApiDaoException extends Exception{

    private static final long serialVersionUID = 7840964671076311761L;
    
    private String destination;
    private String parameters;
    private String result;
    
    public ApiDaoException(String string, Throwable e) {
        super(string, e);
    }
    
    public ApiDaoException(String string){
        super(string);
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getParameters() {
        return this.parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
