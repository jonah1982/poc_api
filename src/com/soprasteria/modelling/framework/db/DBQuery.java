package com.soprasteria.modelling.framework.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.exception.InvalidQueryException;

public abstract class DBQuery {
	public abstract void validate() throws InvalidQueryException;
	
	public abstract DBObject toMongoQuery() throws Exception;
	
	public static DBObject toMongoRangeStmt(Object from, Object to) {
		DBObject doc = new BasicDBObject();
		doc.put("$gte", from);
		doc.put("$lte", to);
		return doc;
	}

	public static DBObject toMongoRangeStmtToNotIn(Object from, Object to) {
		DBObject doc = new BasicDBObject();
		doc.put("$gte", from);
		doc.put("$lt", to);
		return doc;
	}

	public static DBObject toMongoRangeStmtEdgeNotIn(String from, String to) {
		DBObject doc = new BasicDBObject();
		doc.put("$gt", from);
		doc.put("$lt", to);
		return doc;
	}
	
	public static DBObject toBasicFindByKey(String field, Object value) {
		DBObject doc = new BasicDBObject();
		doc.put(field, value);
		return doc;
	}
}
