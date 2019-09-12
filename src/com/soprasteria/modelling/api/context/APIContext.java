package com.soprasteria.modelling.api.context;

import com.soprasteria.modelling.framework.context.ApplicationContext;
import com.soprasteria.modelling.service.ProjectServiceContext;
import com.soprasteria.modelling.service.ServiceContext;
import com.soprasteria.modelling.service.base.entity.UserDomain;

public class APIContext extends ApplicationContext {
	private UserDomain user;
	
	public APIContext() {
		super();
	}

	public UserDomain getUser() {
		return user;
	}

	public void setUser(UserDomain user) {
		this.user = user;
	}
	
	public ProjectServiceContext toProjectContext() throws Exception {
		System.out.println(user.getToken().getProject());
		ProjectServiceContext psc = new ProjectServiceContext(user.getToken().getProject());
		psc.setUser(getUser());
		return psc;
	}
	
	public ServiceContext toServiceContext() {
		ServiceContext sc = new ServiceContext();
		sc.setUser(getUser());
		return sc;
	}
}
