package com.soprasteria.modelling.framework.context;

public interface TransactionITF {
	public void startTrans() throws Exception;
	
	public void rollback();
	
	public void closeTrans();
	
	public void commit();
	
	public boolean inTrans();
}
