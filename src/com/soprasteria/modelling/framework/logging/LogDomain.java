package com.soprasteria.modelling.framework.logging;

import java.util.Date;

import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.entity.domain.MongoDomain;

public class LogDomain extends MongoDomain {
	public final static String collectionName = "log";
	
	public static String COL_LOGGERID = "logger"; // the entity that trigger the logging, the entity can only generate a log at one time 
	public static String COL_TYPE = "type"; // success, error, info
	public static String COL_APPLICATION = "application"; // the application or system logging the log
	public static String COL_MESSAGE = "message";
	
	public static String toOid(String logger) {
		return logger+"-"+new Date().getTime();
	}
	
	public LogDomain(String logger) {
		super(collectionName, toOid(logger));
		setField(COL_LOGGERID, logger);
	}
	
	public LogDomain(String application, String logger, String type, String message) {
		this(logger);
		setField(COL_APPLICATION, application);
		setField(COL_TYPE, type);
		setField(COL_MESSAGE, message);
	}
	
	public LogDomain(DBObject doc) throws Exception {
		super(collectionName, doc);
	}
	
	public String getLogger() {
		return getStringField(COL_LOGGERID);
	}
	
	public String getApplication() {
		return getStringField(COL_APPLICATION);
	}
	
	public String getMessage() {
		return getStringField(COL_MESSAGE);
	}
	
	public String getType() {
		return getStringField(COL_TYPE);
	}
}
