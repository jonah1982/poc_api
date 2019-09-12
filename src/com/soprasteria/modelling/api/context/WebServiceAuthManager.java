package com.soprasteria.modelling.api.context;

import com.soprasteria.modelling.framework.config.PropertyFileSetting;
import com.soprasteria.modelling.framework.context.ApplicationContext;
import com.soprasteria.modelling.framework.exception.AuthException;
import com.soprasteria.modelling.service.base.ProjectService;
import com.soprasteria.modelling.service.base.UserService;
import com.soprasteria.modelling.service.base.entity.TokenDomain;
import com.soprasteria.modelling.service.base.entity.UserDomain;

public class WebServiceAuthManager {
	private UserService userService = new UserService();
	private ProjectService projectService = new ProjectService();

	public UserDomain authenUser(String accesstoken) throws Exception{
		//		return userService.validateToken(accesstoken);
		UserDomain user = null;
		if(accesstoken.contains(PropertyFileSetting.getSetting("accesstoken.test", null))){
			user = new UserDomain("test");
			TokenDomain tokendomain = new TokenDomain(accesstoken);
			user.setToken(tokendomain);
		} else {
			try {
				if(accesstoken.contains(";")) {
					String[] tokens = accesstoken.split(";");
					user = userService.validateToken(tokens[0]);
				} else user = userService.validateToken(accesstoken);
			} catch (Exception e) {
				throw new AuthException("Fail to authenticate user's access token.", e);
			}
		}
		if(accesstoken.contains(";")) {
			String[] tokens = accesstoken.split(";");
			for (int i = 1; i < tokens.length; i++) {
				if(tokens[i].startsWith("p:")) {
					user.getToken().setProjectid(tokens[i].replaceAll("p:", ""));
				}
			}
		}
		
		String projectId = user.getToken().getProjectid();
		if(projectId != null) {
			try {
				user.getToken().setProject(projectService.getProject(projectId));
			} catch (Exception e) {
				e.printStackTrace();
				new ApplicationContext().log("Fail to get project "+projectId);
			}
		}

		return user;
	}

	public UserDomain switchProject(String accesstoken, String projectId) throws Exception{
		return userService.validateToken(accesstoken, projectId);
	}

	public UserDomain authenUser(String username, String password) throws Exception {
		return userService.authUser(username, password);
	}
}
