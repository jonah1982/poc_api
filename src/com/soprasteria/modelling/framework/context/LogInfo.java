package com.soprasteria.modelling.framework.context;

public class LogInfo {
	private String code;
	private String message;
	private Exception details;
	private Object object; // the object that causes the exception.
	private char type;

	public static char TYPE_DEBUG = 'D'; // log4j debug level
	public static char TYPE_APPLICATION = 'A'; // log4j info level
	public static char TYPE_ERROR = 'E'; // log4j error level
	public static char TYPE_AUDIT = 'U'; // call audit service to log into db.

	public LogInfo(String code, String message, Exception details,
			char type, Object object) {
		super();
		this.code = code;
		this.message = message;
		this.details = details;
		this.type = type;
		this.object = object;
	}

	public LogInfo(String message) {
		this(null, message, null, TYPE_APPLICATION, null);
	}

	public LogInfo(Exception details) {
		this(null, details.getMessage(), details, TYPE_ERROR, null);
	}

	public LogInfo(String message, Exception details) {
		this(null, message, details, TYPE_ERROR, null);
	}

	public LogInfo(String message, Exception details, Object object) {
		this(null, message, details, TYPE_ERROR, object);
	}

	public LogInfo(String message, char type) {
		this(null, message, null, type, null);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Exception getDetails() {
		return details;
	}

	public void setDetails(Exception details) {
		this.details = details;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public String toString() {
		if (details != null) return details.toString();
		return message;
	}

}
