package com.soprasteria.modelling.framework.context;

import java.net.UnknownHostException;
import java.util.Hashtable;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.soprasteria.modelling.framework.util.Tool;

public class MongoPoolableTransaction implements TransactionITF {
	public static Hashtable<String, DB> mongopool = new Hashtable<>();
	
	private String host, dbName;
	private int port = 27017; // default mongo port
	
	public DB getDb() {
		return mongopool.get(toString());
	}
	
	public MongoPoolableTransaction(String dbsetting) throws Exception { // mongo1:data:27018
		if(Tool.isEmpty(dbsetting)) throw new Exception("Mongo setting is empty.");
		try {
			this.host = dbsetting.split(":")[0];
		} catch (Exception e) {
			throw new Exception("Mongo setting is invalid for host in "+dbsetting+" due to "+e.getMessage());
		}
		try {
			this.dbName = dbsetting.split(":")[1];
		} catch (Exception e) {
			throw new Exception("Mongo setting is invalid for db in "+dbsetting+" due to "+e.getMessage());
		}
		try {
			this.port = Integer.parseInt(dbsetting.split(":")[2]);
		} catch (Exception e) {
		}
	}

	public MongoPoolableTransaction(String host, String dbName, int port) {
		this.host = host;
		this.dbName = dbName;
		this.port = port;
	}

	@Override
	public void startTrans() throws UnknownHostException, MongoException {
//		if(!inTrans()) {
//			mongo = new MongoClient(this.host, this.port);
//			db = mongo.getDB(dbName);
//		}
		if(!inTrans()) {
			DB db = new MongoClient(this.host, this.port).getDB(dbName);
			mongopool.put(toString(), db);
		}
	}

	@Override
	public void closeTrans() {
//		try {
//			mongo.close();
//			mongo = null;
//		} catch (Exception e) {
//		}
	}

	@Override
	public boolean inTrans() {
//		return !Tool.isEmpty(mongo);
		return mongopool.containsKey(toString());
	}

	@Override
	public void commit() {}

	@Override
	public void rollback() {}
	
	public String toString() {
		return "mongodb:"+host+":"+port+"/"+dbName;
	}

}
