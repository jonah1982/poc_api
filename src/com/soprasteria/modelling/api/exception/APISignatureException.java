package com.soprasteria.modelling.api.exception;

public class APISignatureException extends APIException {
	private static final long serialVersionUID = 1602682367922094798L;
	
	public static String CODE_SECURITY_KEY = "Exception security key";
	public static String CODE_ALGORITHM = "Exception algorithm";
	public static String CODE_SIGNATURE = "Exception signature";
	public static String CODE_VERIFY = "Exception verify";

	public APISignatureException(String errorMessage, String errorCode) {
		super(errorMessage, TYPE_SIGNATURE ,errorCode);
	}

}
