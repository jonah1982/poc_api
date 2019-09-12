package com.soprasteria.modelling.service;

import com.soprasteria.modelling.framework.context.MongoTransaction;
import com.soprasteria.modelling.service.base.entity.ProjectDomain;

public class ProjectServiceContext extends ServiceContext {
	public final static String MONGODB_PROJECT_PREFIX = "poo";
	
	private ProjectDomain project;

	public ProjectServiceContext(ProjectDomain project) throws Exception {
		super();
		this.project = project;
	}
	
	public ProjectServiceContext(String projectId) throws Exception {
		super();
		this.project = new ProjectDomain(projectId);
	}

	public ProjectDomain getProject() {
		return project;
	}
	public MongoTransaction getProjMongo() {
		return super.getMongo(MONGODB_PROJECT_PREFIX+project.getOid());
	}
}
