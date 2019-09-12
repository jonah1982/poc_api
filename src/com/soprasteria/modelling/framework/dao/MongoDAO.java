package com.soprasteria.modelling.framework.dao;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.soprasteria.modelling.framework.context.ApplicationContext;
import com.soprasteria.modelling.framework.context.MongoTransaction;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.soprasteria.modelling.framework.entity.domain.MongoDomain;

public class MongoDAO {
	protected ApplicationContext context;
	private MongoTransaction transaction;
	
	public MongoDAO(ApplicationContext context, MongoTransaction transaction) {
		this.context = context;
		this.transaction = transaction;
	}

	protected ApplicationContext getContext() {
		return context;
	}

	public MongoTransaction getTrans() throws UnknownHostException, MongoException {
		transaction.startTrans();
		return transaction;
	}

	protected DBCollection getCollection(String entityName) throws Exception {
		DBCollection coll = getTrans().getDb().getCollection(entityName);
		if(coll == null)
			coll = getTrans().getDb().createCollection(entityName, null);

		return coll;
	}

	public void update(MongoDomain obj) throws Exception {
		obj.updateLastMod();
		getCollection(obj.getCollectionName()).save(obj.toDBObject());
	}
	
	public void update(MongoAnnoDomain obj) throws Exception {
		obj.updateLastMod();
		DBObject doc = obj.toDBObject();
		getCollection(obj.getCollectionName()).save(doc);
		obj.setOid(doc.get("_id"));
	}

	public void set(MongoDomain obj) throws Exception {
		if(!exist(obj)) update(obj);
		else {
			obj.updateLastMod();
			getCollection(obj.getCollectionName()).update(obj.toFindStmt(), obj.toSetStmt());
		}
	}
	
	public void set(MongoAnnoDomain obj) throws Exception {
		if(!exist(obj)) update(obj);
		else {
			obj.updateLastMod();
			getCollection(obj.getCollectionName()).update(obj.toFindStmt(), obj.toSetStmt());
		}
	}
	
	/**
	 * set object only when it exists
	 * @param obj
	 * @throws Exception
	 */
	public void setExist(MongoDomain obj) throws Exception {
		if(exist(obj)){
			obj.updateLastMod();
			getCollection(obj.getCollectionName()).update(obj.toFindStmt(), obj.toSetStmt());
		} else throw new Exception("This obj"+ obj.getOid()+" doesnt exist.");
	}
	
	/**
	 * set object only when it exists
	 * @param obj
	 * @throws Exception
	 */
	public void setExist(MongoAnnoDomain obj) throws Exception {
		if(exist(obj)){
			obj.updateLastMod();
			getCollection(obj.getCollectionName()).update(obj.toFindStmt(), obj.toSetStmt());
		} else throw new Exception("This obj "+ obj.getOid()+" doesnt exist.");
	}
	
	public void setField(MongoAnnoDomain obj, DBObject setStmt) throws Exception {
		if(exist(obj)){
			obj.updateLastMod();
			getCollection(obj.getCollectionName()).update(obj.toFindStmt(), setStmt);
		} else throw new Exception("This obj"+ obj.getOid()+" doesnt exist.");
	}

	public void batchinsert(String collection, List<DBObject> obj) throws Exception {
		getCollection(collection).insert(obj);
	}

	public void remove(MongoDomain obj) throws Exception {
		getCollection(obj.getCollectionName()).remove(obj.toDBObject());
	}
	
	public void remove(MongoAnnoDomain obj) throws Exception {
		getCollection(obj.getCollectionName()).remove(obj.toFindStmt());
	}

	public boolean exist(MongoDomain obj) throws Exception {
		DBObject doc = new BasicDBObject();
		doc.put(MongoDomain.COL_OID, obj.getOid());

		DBCursor cur = getCollection(obj.getCollectionName()).find(doc);
		if(cur != null && cur.hasNext()) {
			return true;
		}
		return false;
	}
	
	public boolean exist(MongoAnnoDomain obj) throws Exception {
		DBObject doc = null;
		if(obj.getOid() != null)
			doc = obj.toFindStmt();
		else {
			try {
				doc = obj.toFindByKeyStmt();
			} catch (Exception e) {
				return false;
			}
		}

		DBCursor cur = getCollection(obj.getCollectionName()).find(doc);
		if(cur != null && cur.hasNext()) {
			DBObject exist = cur.next();
			obj.setOid(exist.get("_id"));
			return true;
		}
		return false;
	}
	
	public DBObject findOne(String collectionName) throws Exception {
		return getCollection(collectionName).findOne();
	}
	
	public void findOne(MongoDomain template) throws Exception {
		DBObject doc = new BasicDBObject();
		doc.put(MongoDomain.COL_OID, template.getOid());

		DBCursor cur = getCollection(template.getCollectionName()).find(doc);
		if(cur != null && cur.hasNext()) {
			DBObject exist = cur.next();
			template.replicate(exist);
		}
	}
	
	public void findOne(MongoAnnoDomain template) throws Exception {
		DBObject doc = null;
		if(template.getOid() != null) {
			doc = template.toFindStmt();
			context.log("find oid doc:" + doc);
		}
		else {
			try {
				doc = template.toFindByKeyStmt();
				context.log("doc:" + doc);
			} catch (Exception e) {
				throw new Exception("no key find in "+template);
			}
		}

		DBCursor cur = getCollection(template.getCollectionName()).find(doc);
		if(cur != null && cur.hasNext()) {
			DBObject exist = cur.next();
			template.merge(exist);
		}
	}
	
	public void find(DBObject query, MongoDocReader reader) throws Exception {
		DBCursor cur = getCollection(reader.getCollectionName()).find(query);
		while(cur != null && cur.hasNext()) {
			DBObject doc = cur.next();
			reader.read(doc);
		}
	}
	
	public void find(DBObject query, DBObject projection, MongoDocReader reader) throws Exception {
		DBCursor cur = getCollection(reader.getCollectionName()).find(query, projection);
		while(cur != null && cur.hasNext()) {
			DBObject doc = cur.next();
			reader.read(doc);
		}
	}
	
	public void find(DBObject query, DBObject projection, DBObject orderby, MongoDocReader reader) throws Exception {
		DBCursor cur = getCollection(reader.getCollectionName()).find(query, projection).sort(orderby);
		while(cur != null && cur.hasNext()) {
			DBObject doc = cur.next();
			reader.read(doc);
		}
	}
	
	public void findAll(MongoDocReader reader) throws Exception {
		DBCursor cur = getCollection(reader.getCollectionName()).find().limit(200);
		while(cur != null && cur.hasNext()) {
			DBObject doc = cur.next();
			reader.read(doc);
		}
	}
	
	public void findLimit(DBObject query, MongoDocReader reader, int limit) throws Exception {
		DBObject sort = new BasicDBObject();
		sort.put("_id", -1);
		DBCursor cur = getCollection(reader.getCollectionName()).find(query).sort(sort).limit(limit);
		context.log("MongoDAO.findLimit: "+cur.getQuery()+":limit("+limit+")"+" --> result("+cur.count()+")");
		while(cur != null && cur.hasNext()) {
			DBObject doc = cur.next();
			reader.read(doc);
		}
	}
}
