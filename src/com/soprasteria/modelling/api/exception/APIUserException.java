package com.soprasteria.modelling.api.exception;

public class APIUserException extends APIException{
	private static final long serialVersionUID = -4893617741826284335L;
	
	public static String CODE_USER = "Exception username";
	public static String CODE_PASS = "Exception password";
	public static String CODE_MISSING = "Exception missing";
	public static String CODE_RETRIEVE = "Exception retrieval";
	public static String CODE_AUTH = "Exception authentication";
	public static String CODE_RANK = "Exception rank";

	public APIUserException(String errorMessage, String errorCode) {
		super(errorMessage, TYPE_USER ,errorCode);
	}

}
