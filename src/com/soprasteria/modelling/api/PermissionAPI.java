package com.soprasteria.modelling.api;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.soprasteria.modelling.service.base.PermissionService;

@Path("/permission")
public class PermissionAPI extends BaseAPI {
	private PermissionService permissionService = new PermissionService();
	
	@GET
	@Path("/allapps")
	@Produces({MediaType.APPLICATION_JSON})
	public String getAllAPPS(@QueryParam("customerid") String customerid) throws Exception {
		//TODO: if customer has specific apps setting, use the list for that customer
		permissionService.listAllApps();
		HashMap<String, Object> result = new HashMap<>();
		result.put("apps", permissionService.fillUpAppDetails(permissionService.listAllApps()));
		return toHashMapJson(result);
	}
}
