package com.soprasteria.modelling.api.context;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.soprasteria.modelling.framework.exception.AuthException;

@Provider
public class APIExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception arg0) {
		System.out.println("DataModel api failed due to : "+arg0.getMessage());
		arg0.printStackTrace();
		APIError result=new APIError(arg0);
		
		if(arg0 instanceof AuthException)
			return Response.status(Status.UNAUTHORIZED).entity(new Gson().toJson(result)).build();
		
		return Response.ok(new Gson().toJson(result), MediaType.APPLICATION_JSON).build();
	}
}
