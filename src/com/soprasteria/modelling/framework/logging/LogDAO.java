package com.soprasteria.modelling.framework.logging;

import java.util.ArrayList;
import java.util.List;

import com.soprasteria.modelling.framework.context.ApplicationContext;
import com.soprasteria.modelling.framework.context.MongoTransaction;
import com.soprasteria.modelling.framework.dao.MongoDAO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class LogDAO extends MongoDAO {
	private static ApplicationContext context = new ApplicationContext();
	private static MongoTransaction transaction = MongoTransaction.getInstance("dws0", "log", 27017);
	
	public LogDAO() {
		super(context, transaction);
	}
	
	public List<LogDomain> findLatest() throws Exception {
		List<LogDomain> result = new ArrayList<>();
		DBObject doc = new BasicDBObject();

		DBCursor cur = getCollection(LogDomain.collectionName).find(doc).limit(20);
		while(cur != null && cur.hasNext()) {
			DBObject r = cur.next();
			try {
				result.add(new LogDomain(r));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
