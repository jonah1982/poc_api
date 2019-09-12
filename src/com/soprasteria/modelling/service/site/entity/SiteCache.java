package com.soprasteria.modelling.service.site.entity;

import java.util.HashMap;

public class SiteCache {
	private String project;
	private HashMap<String, GenericSiteDomain> sitecacheById = new HashMap<String, GenericSiteDomain>();
	private HashMap<String, GenericSiteDomain> sitecacheByDevice = new HashMap<String, GenericSiteDomain>();
	public SiteCache(String project) {
		super();
		this.project = project;
	}
	
	public String getProject() {
		return project;
	}

	public void addSite(GenericSiteDomain domain) {
		sitecacheById.put(domain.getOid().toString(), domain);
		sitecacheByDevice.put(domain.getDeviceRef(), domain);
	}
	
	public GenericSiteDomain getSite(String siteId) throws Exception {
		return sitecacheById.get(siteId);
	}
	
	public GenericSiteDomain getSiteFromDevice(String deviceId) throws Exception {
		return sitecacheByDevice.get(deviceId);
	}
}
