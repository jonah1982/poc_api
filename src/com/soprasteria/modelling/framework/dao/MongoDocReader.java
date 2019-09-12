package com.soprasteria.modelling.framework.dao;

import com.mongodb.DBObject;

public abstract class MongoDocReader {
	public abstract void read(DBObject doc) throws Exception;
	public abstract String getCollectionName();
}
