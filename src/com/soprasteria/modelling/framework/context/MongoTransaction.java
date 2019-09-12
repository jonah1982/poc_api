package com.soprasteria.modelling.framework.context;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.soprasteria.modelling.framework.util.Tool;

public class MongoTransaction implements TransactionITF {
	private static Hashtable<MongoTransaction, MongoTransaction> mongopool = new Hashtable<>();
	
	protected String host, dbName;
	protected int port = 27017; // default mongo port
	private MongoClient mongo;
	private DB db;
	private Date created = new Date();

	public DB getDb() throws Exception {
		if(db == null) throw new Exception("Null DB in Mongo Transaction: "+this);
		return db;
	}
	
	public static HashMap<String, Object> getPool() {
		HashMap<String, Object> poolString = new HashMap<>();
		
		for(MongoTransaction s: mongopool.keySet()) {
			Object o = mongopool.get(s);
			poolString.put(s.toString(), o.toString());
		}
		
		return poolString;
	}
	
	public static MongoTransaction getInstance(String host, String dbName, int port) {
		MongoTransaction trans = new MongoTransaction(host, dbName, port);
		
		if(!mongopool.contains(trans)) {
			new ApplicationContext().logDebug("-------------------> new mongo "+trans);
			mongopool.put(trans, trans);
		} else {
			trans = mongopool.get(trans);
		}
		
		return trans;
	}
	
	public static MongoTransaction getInstance(String host, String dbName) {
		return getInstance(host, dbName, 27017);
	}
	
	public static MongoTransaction getInstance(String dbsetting) throws Exception {
		MongoTransaction trans = new MongoTransaction(dbsetting);
		
		if(!mongopool.contains(trans)) {
			new ApplicationContext().logDebug("++-----------------> new mongo "+trans);
			mongopool.put(trans, trans);
		} else {
			trans = mongopool.get(trans);
		}
		
		return trans;
	}
	
	protected MongoTransaction(String dbsetting) throws Exception { // mongo1:data:27018
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

	protected MongoTransaction(String host, String dbName, int port) {
		this.host = host;
		this.dbName = dbName;
		this.port = port;
	}
	
	protected MongoTransaction(String host, String dbName) {
		this(host, dbName, 27017);
	}
	
	private void validateDBName(List<String> dbNames) throws MongoException {
		if(!dbNames.contains(dbName)) throw new MongoException("MongoDB "+dbName+ " doesnt exist for "+this);
	}

	@Override
	public void startTrans() throws UnknownHostException, MongoException {
		if(!inTrans()) {
			mongo = new MongoClient(this.host, this.port);
			List<String> dbNames = mongo.getDatabaseNames();
			validateDBName(dbNames);
			db = mongo.getDB(dbName);
			if(db == null) throw new MongoException("Fail to retrieve DB "+ dbName + " from "+this);
		}
		new ApplicationContext().log("Start MongoTransaction "+this);
	}

	@Override
	public void rollback() {
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
		return !Tool.isEmpty(mongo);
	}

	@Override
	public void commit() {
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbName == null) ? 0 : dbName.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MongoTransaction other = (MongoTransaction) obj;
		if (dbName == null) {
			if (other.dbName != null)
				return false;
		} else if (!dbName.equals(other.dbName))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	public String toString() {
		return "mongodb:"+host+":"+port+":"+dbName+" "+created;
	}

}
