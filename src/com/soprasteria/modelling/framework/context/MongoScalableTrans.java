package com.soprasteria.modelling.framework.context;

import java.net.UnknownHostException;

import com.mongodb.MongoException;

public class MongoScalableTrans extends MongoTransaction {

	public MongoScalableTrans(String dbName) {
		super("mongo2", dbName);
	}
	
	public void nextScalable() throws UnknownHostException, MongoException {
		closeTrans();
		
		if("mongo2".equalsIgnoreCase(super.host)) super.host = "mongo3";
		else super.host = "dwsbkp1";
		
		new ApplicationContext().log("loading next MongoScalableTrans: "+host);
		startTrans();
	}

}
