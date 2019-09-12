package com.soprasteria.modelling.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.dao.MongoDocReader;
import com.soprasteria.modelling.framework.entity.domain.MongoDomain;
import com.soprasteria.modelling.framework.logging.LogDAO;
import com.soprasteria.modelling.framework.logging.LogDomain;

@Path("/logview")
public class LogViewAPI extends BaseAPI {
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String getLastLog() throws Exception {
		LogDAO logdao = new LogDAO();
		List<LogDomain> result = logdao.findLatest();
		return toResponse(result);
	}
	
	@GET
	@Path("/{loggerid}/{to}")
	@Produces({MediaType.APPLICATION_JSON})
	public String getLastLog(@PathParam("loggerid") String nodeId, @PathParam("to") Long to) throws Exception {
		LogDAO logdao = new LogDAO();
		final List<LogDomain> result = new ArrayList<>();
		
		MongoDocReader reader = new MongoDocReader() {
			@Override
			public void read(DBObject doc) {
				try {
					result.add(new LogDomain(doc));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public String getCollectionName() {
				return "log";
			}
		};
		
		DBObject doc = new BasicDBObject();
		DBObject doc2 = new BasicDBObject();
		doc2.put("$gte", nodeId+"_"+0);
		doc.put(MongoDomain.COL_OID, doc2);
		doc2.put("$lte", nodeId+"_"+to);
		doc.put(MongoDomain.COL_OID, doc2);
		
		System.out.println("find "+doc);
		
		logdao.findLimit(doc, reader, 100);
		return toResponse(result);
	}
}
