package com.soprasteria.modelling.framework.exception;

public class AuthoException extends Exception {
	private static final long serialVersionUID = -1551757796315508650L;

	public AuthoException(String message) {
        super(message);
    }

    public AuthoException(String message, Exception rootcause) {
        super(message, rootcause);
    }
}
