package com.soprasteria.modelling.api.exception;

public class APITimestampException extends APIException {
	private static final long serialVersionUID = -5871142885848806782L;
	
	public static String CODE_EXPIRED = "Exception expired timestamp";

	public APITimestampException(String errorMessage, String errorCode) {
		super(errorMessage, TYPE_TIMESTAMP ,errorCode);
	}

}
