package com.soprasteria.modelling.api.exception;

public class APITokenException extends APIException{
	private static final long serialVersionUID = 1521466980248219496L;
	
	public static String CODE_USER_ID = "Exception user id";
	public static String CODE_APP_ID = "Exception app id";
	public static String CODE_CLIENT_ID = "Exception client id";
	public static String CODE_TOKEN = "Exception token";

	public APITokenException(String errorMessage, String errorCode) {
		super(errorMessage, TYPE_TOKEN ,errorCode);
	}

}
