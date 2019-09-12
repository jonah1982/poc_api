package com.soprasteria.modelling.api.exception;

public class UnAuthorizedException extends Exception {
	private static final long serialVersionUID = -3186653375868503850L;

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public UnAuthorizedException(String message) {
    	super(message);
    }
}
