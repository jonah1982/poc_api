package com.soprasteria.modelling.service.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.dao.MongoDAO;
import com.soprasteria.modelling.framework.dao.MongoDocReader;
import com.soprasteria.modelling.service.ProjectServiceContext;
import com.soprasteria.modelling.service.ServiceContext;
import com.soprasteria.modelling.service.base.entity.ProjectDomain;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ProjectService {
	private ServiceContext context = new ServiceContext();
	private MongoDAO dao = new MongoDAO(context, context.getBaseMongo());
	
	public static void validateProjectId(Object projectId) throws Exception {
		if(projectId == null) throw new Exception("empty project id");
		if(!StringUtils.isAlphanumeric(projectId.toString())) throw new Exception("project id must be alphanumeric.");
		if(!(projectId.toString().equals(projectId.toString().toLowerCase()))) throw new Exception("project id must be all lowercase.");
		if(projectId.toString().startsWith(ProjectServiceContext.MONGODB_PROJECT_PREFIX)) throw new Exception("invalid project id prefix.");
	}
	
	public void updateProject (ProjectDomain project) throws Exception {
		validateProjectId(project.getOid());
		dao.set(project);
	}
	
	public void addProject (String projectId, String projectName, String timezone,String starttime,String endtime,String address,String status,String clientName,double lat, double lng, int level) throws Exception {
		validateProjectId(projectId);
		ProjectDomain project = new ProjectDomain(projectId);
		project.setName(projectName);
		project.setTimezone(timezone);
		project.setStart_time(starttime);
		project.setEnd_time(endtime);
		project.setAddress(address);
		project.setStatus(status);
		project.setClient_name(clientName);
		project.setMapInfo(lat, lng, level);
		dao.set(project);
	}
			
	public ProjectDomain getProject(String projectId) throws Exception {
		ProjectDomain domain = new ProjectDomain(projectId);
		dao.findOne(domain);
		return domain;
	}
	
	public HashMap<String, ProjectDomain> getAllProjects() throws Exception {
		final HashMap<String, ProjectDomain> result = new HashMap<>();
		MongoDocReader reader = new MongoDocReader() {
			@Override
			public void read(DBObject doc) throws Exception {
				ProjectDomain pd = new ProjectDomain(doc);
				result.put(pd.getOid().toString(), pd);
			}
			
			@Override
			public String getCollectionName() {
				return ProjectDomain.class.getAnnotation(Entity.class).name();
			}
		};
		dao.findAll(reader);
		
		return result;
	}
	
	public HashMap<String, String> getAllProjectName() throws Exception {
		final HashMap<String, String> result = new HashMap<>();
		MongoDocReader reader = new MongoDocReader() {
			@Override
			public void read(DBObject doc) throws Exception {
				ProjectDomain pd = new ProjectDomain(doc);
				result.put(pd.getOid().toString(), pd.getName());
			}
			
			@Override
			public String getCollectionName() {
				return ProjectDomain.class.getAnnotation(Entity.class).name();
			}
		};
		dao.findAll(reader);
		
		return result;
	}
	
	public HashMap<String, ProjectDomain> searchProjects(final HashMap<String, String> searchquery) throws Exception {
		final HashMap<String, ProjectDomain> result = new HashMap<>();
		DBObject query = new BasicDBObject();
		for (String field : searchquery.keySet()) {
			if(!field.equalsIgnoreCase("keyword")) query.put(field, searchquery.get(field));
		}
		MongoDocReader reader = new MongoDocReader() {
			@Override
			public void read(DBObject doc) throws Exception {
				ProjectDomain pd = new ProjectDomain(doc);
				result.put(pd.getOid().toString(), pd);
			}
			
			@Override
			public String getCollectionName() {
				return ProjectDomain.class.getAnnotation(Entity.class).name();
			}
		};
		dao.find(query, reader);
		
		return result;
	}
	
	public List<String> getAllProjectIds() throws Exception {
		final List<String> result = new ArrayList<>();
		MongoDocReader reader = new MongoDocReader() {
			@Override
			public void read(DBObject doc) throws Exception {
				result.add(new ProjectDomain(doc).getOid().toString());
			}
			
			@Override
			public String getCollectionName() {
				return ProjectDomain.class.getAnnotation(Entity.class).name();
			}
		};
		dao.findAll(reader);
		
		return result;
	}
}
