package com.soprasteria.modelling.api;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.soprasteria.modelling.service.base.ProjectService;
import com.soprasteria.modelling.service.base.entity.ProjectDomain;
import com.soprasteria.modelling.service.site.SiteService;
import com.soprasteria.modelling.service.site.entity.WSNSiteDomain;

@Path("/project")
public class ProjectAPI extends BaseAPI {
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON})
	public String getAllProjects(@QueryParam("token") String accesstoken) throws Exception {
		authen(accesstoken);
		
		ProjectService service = new ProjectService();
		HashMap<String, ProjectDomain> result = service.getAllProjects();
		filterProject(result);
		return toResponse(result);
	}
	
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public String searchProject(@QueryParam("token") String accesstoken, @QueryParam("query") String query) throws Exception {
		authen(accesstoken);
		HashMap<String, String> querytable= new Gson().fromJson(query, new TypeToken<HashMap<String, String>>() {
			private static final long serialVersionUID = 3893043968492957462L;}.getType());
		
		ProjectService service = new ProjectService();
		HashMap<String, ProjectDomain> result = service.searchProjects(querytable);
		filterProject(result);
		return toResponse(result);
	}
	
	@GET
	@Path("/detail")
	@Produces({MediaType.APPLICATION_JSON})
	public String getProject(@QueryParam("token") String accesstoken) throws Exception {
		authen(accesstoken);
		ProjectDomain project = context.toProjectContext().getProject();
		SiteService<WSNSiteDomain> service = new SiteService<WSNSiteDomain>(WSNSiteDomain.class, context.toProjectContext());
		int sitecount = 20;
		try {
			sitecount = service.search(new HashMap<String, String>()).size();
		} catch (Exception e) {
		}
		project.setSensorCount("pressure", sitecount);
		project.setSensorCount("flow meter", 0);
		project.setSensorCount("water quality", 0);
		return toResponse(project);
	}
	
	private void filterProject(HashMap<String, ProjectDomain> result) {
		HashMap<String, String> projids = context.getUser().getProjects();
		for (String projectoid : result.keySet()) {
			if(projids.containsKey(projectoid)) result.remove(projectoid);
		}
	}
	
	
}
