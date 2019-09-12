package com.soprasteria.modelling.api.exception;

public class APIException extends Exception {
	private static final long serialVersionUID = -7611290272626555288L;
	private String errorType;
	private String errorCode;
	
	public static String TYPE_TIMESTAMP = "Timestamp Error";
	public static String TYPE_SIGNATURE = "Signature Error";
	public static String TYPE_TOKEN = "Token Error";
	public static String TYPE_USER = "User Error";
	
	public APIException(String errorMessage) {
		this(errorMessage, null, null);
	}
	
	public APIException(String errorMessage, String errorType, String errorCode) {
		super(errorMessage);
		this.errorType = errorType;
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return super.getMessage();
	}
	
	public String getErrorType() {
		return this.errorType;
	}
	
	public String getErrorCode() {
		return this.errorCode;
	}
	
	public boolean isEqualType(String type) {
		if(this.errorType.equals(type)) {
			return true;
		}
		
		return false;
	}

}
