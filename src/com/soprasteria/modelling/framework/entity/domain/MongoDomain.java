package com.soprasteria.modelling.framework.entity.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.util.Tool;
import com.google.gson.Gson;

public class MongoDomain {
	public static String COL_OID = "_id";
	public static String COL_LASTMOD = "lastmod";
	public static String COL_CREATED = "created";

	private String collectionName;
	private DBObject doc;

	public MongoDomain(String collectionName, Object oid) {
		this.collectionName = collectionName;
		this.doc = new BasicDBObject();
		setField(COL_OID, oid);
		updateLastMod();
		setField(COL_CREATED, new Date());
	}
	
	public MongoDomain(String collectionName) {
		this(collectionName, new ObjectId());
	}
	
	public MongoDomain(String collectionName, DBObject doc) throws Exception {
		Object oid = doc.get(COL_OID);
		
		if(Tool.isEmpty(oid)) throw new Exception("the domain to be created doesnt have oid.");
		this.collectionName = collectionName;
		this.doc = new BasicDBObject();
		setField(COL_OID, oid);
		updateLastMod();
		setField(COL_CREATED, new Date());
		
		merge(doc);
	}

	public String getCollectionName() {
		return collectionName;
	}

	public Object getField(String field) {
		return doc.get(field);
	}

	public void setField(String field, Object value) {
		doc.put(field, value);
	}
	
	public void clear() {
		DBObject doc2 = new BasicDBObject();
		doc2.put(COL_OID, doc.get(COL_OID));
		doc2.put(COL_LASTMOD, doc.get(COL_LASTMOD));
		doc2.put(COL_CREATED, doc.get(COL_CREATED));
		
		this.doc = doc2;
	}
	
	public void merge (DBObject doc) {
		this.doc.putAll(doc);
	}
	
	public void replicate (DBObject doc) {
		clear();
		this.doc.putAll(doc);
	}

	public String getStringField(String field) {
		Object o = doc.get(field);

		if (o != null) {
			//System.out.println(o.toString());
			return o.toString();
		}
		return "";
	}

	public Long getLongField(String field) {
		Object o = doc.get(field);

		try {
			if (o != null) {
				if (o instanceof Long)
					return (Long) o;
				else if (o instanceof Double)
					return ((Double) o).longValue();
				else
					return Long.parseLong(o.toString());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public Integer getIntField(String field) {
		Object o = doc.get(field);

		try {
			if (o != null) {
				if (o instanceof Integer)
					return (Integer) o;
				else if (o instanceof Double)
					return ((Double) o).intValue();
				else
					return Integer.parseInt(o.toString());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Date getDateField(String field) {
		Object o = doc.get(field);

		if (o != null) {
			if (o instanceof Date)
				return (Date) o;
			else
				return null;
		}

		return null;
	}

	public BigDecimal getDoubleField(String field) {
		Object o = doc.get(field);

		try {
			if (o != null) {
				if (o instanceof Double)
					return new BigDecimal((Double) o);
				else
					return new BigDecimal(o.toString());
			}
		} catch (NumberFormatException e) {
		}

		return null;
	}

	public DBObject toDBObject() {
		return doc;
	}

	public Object getOid() {
		return getField(COL_OID);
	}

	public String toString() {
		return new Gson().toJson(this);
	}

	protected void setLastMod(Date timestamp) {
		setField(COL_LASTMOD, timestamp);
	}

	public void updateLastMod() {
		setLastMod(new Date());
	}

	public Date getLastMod() {
		Object o= getField(COL_LASTMOD);
		if(o instanceof Long) return new Date((Long)o);
		else return getDateField(COL_LASTMOD);
	}

	public Date getCreated() {
		Object o= getField(COL_CREATED);
		if(o instanceof Long) return new Date((Long)o);
		else return getDateField(COL_CREATED);
	}

	public DBObject toUpdateLastModStmt() {
		BasicDBObject obj = new BasicDBObject(COL_LASTMOD,
				new Date());
		return obj;
	}
	
	public DBObject toSetStmt() {
		DBObject doc1 = new BasicDBObject();
		DBObject doc2 = new BasicDBObject();
		
		String[] keys = (String[]) doc.keySet().toArray(new String[0]);

		for (String key : keys) {
			if(key.equals("_id")) continue;
			if(key.equals(COL_CREATED)) continue;
			doc2.put(key, doc.get(key));
		}

		doc2.putAll(toUpdateLastModStmt());

		doc1.put("$set", doc2);
		return doc1;
	}
	
	public DBObject toFindStmt() {
		DBObject findDoc = new BasicDBObject();
		Object oid = getOid();
		findDoc.put(MongoDomain.COL_OID, oid);
		
		return findDoc;
	}
}
