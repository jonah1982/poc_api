package com.soprasteria.modelling.framework.exception;

public class ServiceException extends Exception {
	private static final long serialVersionUID = -7611290272626555288L;
	private String errorType;
	private String errorCode;
	
	public ServiceException(String errorMessage) {
		this(errorMessage, null, null);
	}
	
	public ServiceException(String errorMessage, String errorType, String errorCode) {
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
