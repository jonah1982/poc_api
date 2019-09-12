package com.soprasteria.modelling.service.site.entity;

import java.util.HashMap;
import java.util.List;

public class SiteSummary {
	
	private String projectname;

	private List<String> sittypes;
	private List<String> sitestatus;

	private HashMap<String,List<String>> sitetypemap;
	private HashMap<String,List<String>> sitestatusmap;
	private HashMap<String,String> typenamemap;
	private HashMap<String,String> statusnamemap;
	
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public HashMap<String, String> getTypenamemap() {
		return typenamemap;
	}
	public void setTypenamemap(HashMap<String, String> typenamemap) {
		this.typenamemap = typenamemap;
	}
	public HashMap<String, String> getStatusnamemap() {
		return statusnamemap;
	}
	public void setStatusnamemap(HashMap<String, String> statusnamemap) {
		this.statusnamemap = statusnamemap;
	}
	
	public List<String> getSittypes() {
		return sittypes;
	}
	public void setSittypes(List<String> sittypes) {
		this.sittypes = sittypes;
	}
	public List<String> getSitestatus() {
		return sitestatus;
	}
	public void setSitestatus(List<String> sitestatus) {
		this.sitestatus = sitestatus;
	}
	public HashMap<String, List<String>> getSitetypemap() {
		return sitetypemap;
	}
	public void setSitetypemap(HashMap<String, List<String>> sitetypemap) {
		this.sitetypemap = sitetypemap;
	}
	public HashMap<String, List<String>> getSitestatusmap() {
		return sitestatusmap;
	}
	public void setSitestatusmap(HashMap<String, List<String>> sitestatusmap) {
		this.sitestatusmap = sitestatusmap;
	}
	
}
