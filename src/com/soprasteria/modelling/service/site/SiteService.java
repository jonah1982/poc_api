package com.soprasteria.modelling.service.site;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.dao.MongoDocReader;
import com.soprasteria.modelling.service.ProjectServiceContext;
import com.soprasteria.modelling.service.site.dao.SiteCachedDAO;
import com.soprasteria.modelling.service.site.dao.SiteDAO;
import com.soprasteria.modelling.service.site.entity.DataPointDomain;
import com.soprasteria.modelling.service.site.entity.GenericSiteDomain;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class SiteService<T extends GenericSiteDomain> {
	protected ProjectServiceContext context;
	protected SiteDAO<T> dao;
	private Class<T> clazz;
	
	public SiteService(Class<T> clazz, ProjectServiceContext context) {
		this.clazz = clazz;
		this.context = context;
		dao = new SiteCachedDAO<T>(clazz, context, context.getProjMongo());
	}

	public boolean exist(T siteDomain) throws Exception {
		return dao.exist(siteDomain);
	}

	public boolean isDevIdOccupied(String siteId, String deviceId) throws Exception {
		Constructor<T> ctor = clazz.getConstructor(String.class, String.class);
		if (siteId == null){
			T domain = (T)ctor.newInstance(null, deviceId);
			return dao.exist(domain);
		}else {
			HashMap<String, String> searchquery = new HashMap<>();
			searchquery.put("device_ref",deviceId);
			List<T> resulet = search(searchquery);
			for (T domain:resulet){
				if (!domain.getOid().equals(siteId)) return true;
			}
			return false;
		}
	}
	
	public void update(T siteDomain) throws Exception {
		dao.set(siteDomain);
	}
	
	public T getSite(String siteId) throws Exception {
		return dao.getSite(siteId);
	}
	
	public T getSiteFromDevice(String deviceId) throws Exception {
		return dao.getSiteFromDevice(deviceId);
	}
	
	public void addDataPoint(String siteId, String datapointType) throws Exception {
		GenericSiteDomain domain = new GenericSiteDomain(siteId, null);
		dao.findOne(domain);
		domain.addDatapoint(new DataPointDomain(domain, datapointType));
		dao.update(domain);
	}
	
	public void addDataPoint(GenericSiteDomain domain, String datapointType, String unit) throws Exception {
		dao.findOne(domain);
		DataPointDomain dpd = new DataPointDomain(domain, datapointType);
		dpd.setUnit(unit);
		domain.addDatapoint(dpd);
		dao.update(domain);
	}
	
	public DataPointDomain getDataPoint(String datapointId) throws Exception {
		return dao.getDataPoint(datapointId);
	}

	public List<T> search(final HashMap<String, String> searchquery) throws Exception {
		context.log("SiteService: start to search for sites = "+searchquery);
		DBObject query = new BasicDBObject();
		for (String field : searchquery.keySet()) {
			if(!field.equalsIgnoreCase("keyword")) query.put(field, searchquery.get(field));
		}
		context.log("SiteService: query information = "+query);
		
		final List<T> result = new ArrayList<T>();
		MongoDocReader reader = new MongoDocReader() {
			@Override
			public void read(DBObject doc) throws Exception {
				Constructor<T> ctor = clazz.getConstructor(DBObject.class);
				T domain = (T)ctor.newInstance(doc);
				if(domain.containKeyword(searchquery.get("keyword"))){
					result.add(domain);
				}
			}
			
			@Override
			public String getCollectionName() {
				return GenericSiteDomain.class.getAnnotation(Entity.class).name();
			}
		};
		
		try {
			dao.find(query, reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void remove(T siteDomain) throws Exception{
		dao.remove(siteDomain);
	}
	
}
