package com.soprasteria.modelling.api.logger;

import com.soprasteria.modelling.framework.logging.LogDomain;

public class APILog extends LogDomain {
	public static String COL_APINAME = "api_name";
	public static String COL_CALLERIP = "caller_ip";
	public static String COL_PARA = "para";
	public static String COL_RESULT = "result";
	public static String COL_CONTEXTPATH = "context_path";
	public static String COL_DURATION = "duration";
	
	public APILog(String apiname, String callerip, String type, String message, Object para){
		super("api", apiname+"-"+callerip, type, message);
		setField(COL_APINAME, apiname);
		setField(COL_CALLERIP, callerip);
		setField(COL_PARA, para);
	}
	
	public void setSuccess(String returnjson){
		setField(COL_TYPE, "success");
		setField(COL_RESULT, returnjson);
	}
	
	public void setFailed(String errormsg){
		setField(COL_TYPE, "error");
		setField(COL_RESULT, errormsg);
	}
	
	public void setContextPath(String contextPath) {
		setField(COL_CONTEXTPATH, contextPath);
	}
	
	public void setDuration(double duration) {
		setField(COL_DURATION, duration);
	}
}
