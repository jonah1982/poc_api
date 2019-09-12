package com.soprasteria.modelling.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/messages")
public class MessageAPI extends BaseAPI {

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String getMessages(@Context HttpServletRequest request, @QueryParam("token") String token) {
		return toSimpleProperJson("description", "Return all the the messages of a user.");
	}
}
