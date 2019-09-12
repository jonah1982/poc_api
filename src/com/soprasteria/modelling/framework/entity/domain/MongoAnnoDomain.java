package com.soprasteria.modelling.framework.entity.domain;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.context.ApplicationContext;
import com.soprasteria.modelling.framework.util.Tool;

public class MongoAnnoDomain {
	@Column
	private Object _id;
	@Column(isNull=false)
	private Date created;
	@Column(isNull=false)
	private Date lastmod;
	
	public MongoAnnoDomain() {
		created = new Date();
		updateLastMod();
	}
	
	public MongoAnnoDomain(DBObject doc) throws Exception {
		if(getClass().isAnnotationPresent(Entity.class)) {
			List<Field> fields = Tool.getFields(getClass());
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					field.setAccessible(true);
					Object value = doc.get(field.getName());
					if(value != null) {
						try {
							field.set(this, value);
						} catch (Exception e) {
							new ApplicationContext().log("fail to set value to MongoAnnoDomain field "+field.getName()+ " due to "+e.getMessage());
//							e.printStackTrace();
						}
					}
				}
			}
		} else {
			throw new Exception("This domain "+getClass()+" is not defined as Entity");
		}
		if(created == null) created = new Date();
		if(lastmod == null) updateLastMod();
		if(_id == null) throw new Exception("document in "+getCollectionName()+" doesnt have _id.");
	}
	
	public Object getOid() {return _id;}
	
	public void setOid(Object oid){this._id = oid;}
	
	public Date getCreated() {return created;}

	public void setCreated(Date created) {this.created = created;}

	public Date getLastmod() {return lastmod;}

	public void setLastmod(Date lastmod) {this.lastmod = lastmod;}
	
	public void updateLastMod() {setLastmod(new Date());}
	
	public boolean isEmpty() { return _id==null;}

	public String getCollectionName() {
		if(getClass().isAnnotationPresent(Entity.class)) {
			Entity entity = getClass().getAnnotation(Entity.class); 
			String collectionName = entity.name();
			if(Tool.isEmpty(collectionName)) return "";
			
			return collectionName;
		} 
		return "";
	}
	
	public DBObject toDBObject() throws Exception {
		DBObject doc = new BasicDBObject();
		
		if(getClass().isAnnotationPresent(Entity.class)) {
//			Field[] fields = getClass().getDeclaredFields();
			List<Field> fields = Tool.getFields(getClass());
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					field.setAccessible(true);
					Column column = field.getAnnotation(Column.class);
					Object value = field.get(this);
					if(!column.isNull() && value == null) throw new Exception ("Non-nullable field ("+field.getName()+") indicated");
					doc.put(field.getName().toLowerCase(), value);
				}
			}
		} else {
			throw new Exception("This domain "+getClass()+" is not defined as Entity");
		}
		return doc;
	}
	
	/**
	 * Clone the content to the target, except the keys.
	 * @param target
	 * @throws Exception
	 */
	public void clone(MongoAnnoDomain target) throws Exception {
		DBObject doc = toDBObject();
		List<Field> fields = Tool.getFields(getClass());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				if(field.getAnnotation(Column.class).isKey() || field.getName().equalsIgnoreCase("_id")) doc.removeField(field.getName()); // keys will not be cloned
			}
		}
		target.merge(doc);
	}
	
	/**
	 * Merge the whole content from doc to the current object.
	 * @param doc
	 * @throws Exception
	 */
	public void merge(DBObject doc) throws Exception {
		if(getClass().isAnnotationPresent(Entity.class)) {
			List<Field> fields = Tool.getFields(getClass());
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					field.setAccessible(true);
					Object value = doc.get(field.getName());
					if(value != null) {
						try {
							field.set(this, value);
						} catch (Exception e) {
							new ApplicationContext().log("fail to set value to MongoAnnoDomain field "+field.getName()+ " due to "+e.getMessage());
//							e.printStackTrace();
						}
					}
				}
			}
		} else {
			throw new Exception("This domain "+getClass()+" is not defined as Entity");
		}
	}
	
	public String toString() {
		String collectionName = null;
		DBObject content = null;
		try { collectionName = getCollectionName(); } catch (Exception e) {}
		try { content = toDBObject(); } catch (Exception e) {}
		
		return collectionName + "=>" + content;
	}

	public DBObject toFindStmt() throws Exception {
		DBObject findDoc = new BasicDBObject();
		Object oid = getOid();
		if(oid != null) {
			findDoc.put("_id", oid);
			return findDoc;
		} else return toFindByKeyStmt();
	}
	
	public DBObject toFindByKeyStmt() throws Exception { // only look for one key
		DBObject doc = new BasicDBObject();
		if(getClass().isAnnotationPresent(Entity.class)) {
			List<Field> fields = Tool.getFields(getClass());
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					field.setAccessible(true);
					Column column = field.getAnnotation(Column.class);
					if(column.isKey()) {
						Object value = field.get(this);
						doc.put(field.getName().toLowerCase(), value);
						return doc;
					}
				}
			}
		} else {
			throw new Exception("This domain "+getClass()+" is not defined as Entity");
		}
		throw new Exception("This domain "+getClass()+" doesnt have key defined.");
	}
	
	public DBObject toSetStmt() throws Exception {
		updateLastMod();
		DBObject doc1 = new BasicDBObject();
		DBObject doc2 = new BasicDBObject();
		
		List<Field> fields = Tool.getFields(getClass());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				field.setAccessible(true);
				Column column = field.getAnnotation(Column.class);
				Object value = field.get(this);
				if(!column.isNull() && value == null) throw new Exception ("Non-nullable field ("+field.getName()+") indicated");
				
				if(field.getName().equals("_id")) continue;
				if(field.getName().equals("created")) continue;
				if(value == null) continue;
				
				doc2.put(field.getName().toLowerCase(), value);
			}
		}

		doc1.put("$set", doc2);
		return doc1;
	}
}
