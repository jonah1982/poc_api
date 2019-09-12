package com.soprasteria.modelling.service.site.dao;

import java.util.HashMap;

import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.context.MongoTransaction;
import com.soprasteria.modelling.framework.dao.MongoDocReader;
import com.soprasteria.modelling.service.ProjectServiceContext;
import com.soprasteria.modelling.service.site.entity.DataPointDomain;
import com.soprasteria.modelling.service.site.entity.GenericSiteDomain;
import com.soprasteria.modelling.service.site.entity.SiteCache;
import com.soprasteria.modelling.service.site.entity.WSNSiteDomain;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SiteCachedDAO<T extends GenericSiteDomain> extends SiteDAO<T> {
	protected static HashMap<String, SiteCache> siteCacheMap = new HashMap<>();
	
	private SiteCache siteCache;
	
	public SiteCachedDAO(Class<T> clazz, ProjectServiceContext context, MongoTransaction transaction) {
		super(clazz, context, transaction);
		String projectid = context.getProject().getOid().toString();
		if(siteCacheMap.containsKey(projectid)) {
			siteCache = siteCacheMap.get(projectid);
		} else {
			siteCache = new SiteCache(projectid);
			siteCacheMap.put(projectid, siteCache);
			loadAllSites();
		}
	}
	
	protected void loadAllSites() {
		DBObject query = new BasicDBObject();
		MongoDocReader reader = new MongoDocReader() {
			@Override
			public void read(DBObject doc) throws Exception {
//				Constructor<T> ctor = clazz.getConstructor(DBObject.class);
//				T domain = (T)ctor.newInstance(doc);
				//TODO: temporarily use WSNSiteDomain
				siteCache.addSite(new WSNSiteDomain(doc));
			}
			
			@Override
			public String getCollectionName() {
				return GenericSiteDomain.class.getAnnotation(Entity.class).name();
			}
		};
		
		try {
			find(query, reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public T getSite(String siteId) throws Exception {
		try {
			return (T)siteCache.getSite(siteId);
		} catch (Exception e) {
			return super.getSite(siteId);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getSiteFromDevice(String deviceId) throws Exception {
		try {
			return (T)siteCache.getSiteFromDevice(deviceId);
		} catch (Exception e) {
			return super.getSiteFromDevice(deviceId);
		}
	}
	
	public DataPointDomain getDataPoint(String datapointId) throws Exception {
		context.log("start to find datapoint for "+datapointId);
		GenericSiteDomain domain = getSite(DataPointDomain.getSiteOid(datapointId));
		if(domain == null || domain.isEmpty()) throw new Exception("site "+DataPointDomain.getSiteOid(datapointId)+" cant be found.");
		context.log("start to find datapoint from site "+domain);
		DataPointDomain dpd = domain.getDatapoint(DataPointDomain.getDataPointType(datapointId));
		if(dpd == null) throw new Exception("Datapoint "+datapointId+" doesnt exist.");
		dpd.setSite(domain);
		return dpd;
	}
}
