package com.soprasteria.modelling.service.base;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBObject;
import com.soprasteria.modelling.service.base.entity.AppDomain;
import com.soprasteria.modelling.service.base.entity.RoleDomain;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.dao.MongoDAO;
import com.soprasteria.modelling.framework.dao.MongoDocReader;
import com.soprasteria.modelling.service.ServiceContext;

public class PermissionService {
	private ServiceContext context = new ServiceContext();
	private MongoDAO dao = new MongoDAO(context, context.getBaseMongo());
	private UserService userService = new UserService();
	
	public List<String> listAllApps() throws Exception {
		final List<String> applist = new ArrayList<>();
		dao.findAll(new MongoDocReader() {
			@Override
			public void read(DBObject doc) throws Exception {
				applist.add(new AppDomain(doc).getOid().toString());
			}
			
			@Override
			public String getCollectionName() {
				return AppDomain.class.getAnnotation(Entity.class).name();
			}
		});
		
		return applist;
	}
	
	public void addApp(String projectId, String appid) throws Exception {
		//TODO: if customer is valid, assign app to the customer
		AppDomain app = new AppDomain(appid.toLowerCase());
		app.setName(appid);
		dao.set(app);
	}
	
	public void grantAccess(String projectId, String roleId, List<String> applist) throws Exception {
		userService.validateProject(projectId);
		RoleDomain role = userService.getRole(roleId);
		if(role == null) {
			role = new RoleDomain(roleId);
		}
		role.setApps(applist);
		
		dao.set(role);
	}
	
	public void grantAccess(String projectId, String roleId, String appid) throws Exception {
		userService.validateProject(projectId);
		
		RoleDomain role = userService.getRole(roleId);
		if(role == null) {
			role = new RoleDomain(roleId);
		}
		role.addApp(appid);
		dao.set(role);
	}
	
	public void removeAccess(String roleId, String appid) throws Exception {
		RoleDomain role = userService.getRole(roleId);
		if(role == null) {
			throw new Exception("The role for "+roleId+" doesnt exist.");
		}
		role.removeApp(appid);
		dao.set(role);
	}
	
	public List<AppDomain> fillUpAppDetails(List<String> appids) throws Exception {
		List<AppDomain> applist = new ArrayList<>();
		
		for (String appid : appids) {
			AppDomain app = new AppDomain(appid);
			dao.findOne(app);
			applist.add(app);
		}
		
		return applist;
	}
}
