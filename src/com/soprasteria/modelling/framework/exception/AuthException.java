package com.soprasteria.modelling.framework.exception;

public class AuthException extends Exception {
	private static final long serialVersionUID = -2272513298089122292L;
	
	public AuthException(String message) {
		super(message);
	}
	
	public AuthException(String message, Exception rootcause) {
		super(message, rootcause);
	}
}
