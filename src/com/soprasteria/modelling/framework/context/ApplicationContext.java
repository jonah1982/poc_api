package com.soprasteria.modelling.framework.context;

import org.apache.log4j.Logger;

public class ApplicationContext implements ContextModelInterface {
	protected Logger logger;

	protected Class<? extends ApplicationContext> caller = this.getClass();
	
	public ApplicationContext() {
		logger = Logger.getLogger("Application");
	}
	
	protected String populatePrefix() {
		return "";
	}
	
	protected void log(LogInfo loginfo) {
		if(loginfo != null) {
			char type = loginfo.getType();
			String content = "["+populatePrefix() + "] " + loginfo;
			if (type == LogInfo.TYPE_ERROR) {
				logger.error(content);
			} else if (type == LogInfo.TYPE_DEBUG) {
				logger.debug(content);
			} else {
				logger.info(content);
			}
		}
	}
	
	public void logError(String code, String message) {
		LogInfo loginfo = new LogInfo(message);
		loginfo.setCode(code);
		loginfo.setType(LogInfo.TYPE_ERROR);
		this.log(loginfo);
	}
	
	public void logException (Exception e) {
		LogInfo loginfo = new LogInfo(e);
		loginfo.setType(LogInfo.TYPE_ERROR);
		this.log(loginfo);
	}
	
	public void logInfo(String code, String message) {
		LogInfo loginfo = new LogInfo(message);
		loginfo.setCode(code);
		loginfo.setType(LogInfo.TYPE_AUDIT);
		this.log(loginfo);
	}
	
	public void logDebug(String message) {
		LogInfo loginfo = new LogInfo(message);
		loginfo.setCode("");
		loginfo.setType(LogInfo.TYPE_DEBUG);
		this.log(loginfo);
	}
	
	public void log(String message) {
//		print(message);
		LogInfo loginfo = new LogInfo(message);
		loginfo.setCode("");
		loginfo.setType(LogInfo.TYPE_AUDIT);
		this.log(loginfo);
	}
	
	@Override
	public void cloneContext(ContextModelInterface context) {
		
	}
	
	public void print(Object o) {
		System.out.println(o);
	}
}
