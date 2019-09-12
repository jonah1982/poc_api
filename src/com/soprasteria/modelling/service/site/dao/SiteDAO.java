package com.soprasteria.modelling.service.site.dao;

import java.lang.reflect.Constructor;

import com.soprasteria.modelling.framework.context.MongoTransaction;
import com.soprasteria.modelling.framework.dao.MongoDAO;
import com.soprasteria.modelling.service.ProjectServiceContext;
import com.soprasteria.modelling.service.site.entity.DataPointDomain;
import com.soprasteria.modelling.service.site.entity.GenericSiteDomain;

public class SiteDAO<T extends GenericSiteDomain> extends MongoDAO {
	protected Class<T> clazz;
	
	public SiteDAO(Class<T> clazz, ProjectServiceContext context, MongoTransaction transaction) {
		super(context, transaction);
		this.clazz = clazz;
	}

	public T getSite(String siteId) throws Exception {
		Constructor<T> ctor = clazz.getConstructor(String.class, String.class);
		T domain = (T)ctor.newInstance(siteId, null);
		findOne(domain);
		return domain;
	}
	
	public T getSiteFromDevice(String deviceId) throws Exception {
		Constructor<T> ctor = clazz.getConstructor(String.class, String.class);
		T domain = (T)ctor.newInstance(null, deviceId);
		findOne(domain);
		return domain;
	}
	
	public DataPointDomain getDataPoint(String datapointId) throws Exception {
		context.log("start to find datapoint for "+datapointId);
		GenericSiteDomain domain = new GenericSiteDomain(DataPointDomain.getSiteOid(datapointId), null);
		findOne(domain);
		if(domain.isEmpty()) throw new Exception("site "+DataPointDomain.getSiteOid(datapointId)+" cant be found.");
		context.log("start to find datapoint from site "+domain);
		DataPointDomain dpd = domain.getDatapoint(DataPointDomain.getDataPointType(datapointId));
		if(dpd == null) throw new Exception("Datapoint "+datapointId+" doesnt exist.");
		dpd.setSite(domain);
		return dpd;
	}
}
