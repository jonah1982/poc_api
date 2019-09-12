package com.soprasteria.modelling.api.context;

import java.net.URI;
import java.util.HashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import com.soprasteria.modelling.api.logger.APILog;
import com.soprasteria.modelling.framework.logging.LogDAO;

public class APIRequestEventListener implements RequestEventListener {
	protected final String requestNumber;
	protected final long startTime;
	private APILog log = null;
	private LogDAO logger = new LogDAO();

	public APIRequestEventListener(String requestNumber) {
		this.requestNumber = requestNumber;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onEvent(RequestEvent event) {
		switch (event.getType()) {
		case RESOURCE_METHOD_START:
			System.out.println("DataModel request " + requestNumber+ " method " + event.getUriInfo().getMatchedResourceMethod().getHttpMethod() + " started.");
			apiCallStart(event);
			break;
		case FINISHED:
			System.out.println("DataModel request " + requestNumber + " finished. Processing time " + (System.currentTimeMillis() - startTime) + " ms.");
			apiCallEnd(event);
			break;
		default:
			break;
		}
	}
	
	protected void apiCallStart(RequestEvent event) {
		try {
			String apiPath = event.getUriInfo().getPath(true);
			String method = event.getUriInfo().getMatchedResourceMethod().getHttpMethod();
			URI baseURI = event.getUriInfo().getBaseUri();
			String caller = requestNumber+"";
			
			HashMap<String, Object> requestContent = new HashMap<String, Object>();
			requestContent.put("method", 		method);
			requestContent.put("path_para", 	event.getUriInfo().getPathParameters());
			requestContent.put("query_para", 	event.getUriInfo().getQueryParameters());
			requestContent.put("base_uri", 		baseURI.toString());
			
			log = new APILog(apiPath, caller, "info", "api call", requestContent);
			log.setContextPath(baseURI.toString());
			logger.set(log);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	protected void apiCallEnd(RequestEvent event) {
		try {
			Throwable error = event.getException();
			if(error == null) {
				event.getContainerResponse().setMediaType(MediaType.APPLICATION_JSON_TYPE);
				MultivaluedMap<String, Object> headers = event.getContainerResponse().getHeaders();
				headers.add("Access-Control-Allow-Origin", "*");
				//headers.add("Access-Control-Allow-Origin", "http://*.org"); //allows CORS requests only coming from *.org		
				headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");			
				headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia, Authorization");
				log.setSuccess(null);
			} else {
				log.setFailed(error.getMessage());
			}
			log.setDuration((System.currentTimeMillis() - startTime));
			logger.set(log);
		} catch (Exception e){
		}
	}
}
