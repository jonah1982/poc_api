package com.soprasteria.modelling.framework.exception;

public class InvalidQueryException extends Exception {
	private static final long serialVersionUID = 5261735726641483820L;
	
	public InvalidQueryException(Class<?> queryclass, String message) {
		super("Invalid "+queryclass.getName()+": "+message);
	}

}
