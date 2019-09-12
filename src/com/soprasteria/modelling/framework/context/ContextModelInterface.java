package com.soprasteria.modelling.framework.context;

public interface ContextModelInterface {

	public void cloneContext(ContextModelInterface context);
	
	public void logInfo(String code, String message);
	public void logException(Exception e);
	
	public void logError(String code, String message);
}
