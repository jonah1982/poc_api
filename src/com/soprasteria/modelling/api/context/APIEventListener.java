package com.soprasteria.modelling.api.context;

import java.net.UnknownHostException;
import java.util.Date;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import com.soprasteria.modelling.framework.util.DateTimeTool;
import com.soprasteria.modelling.framework.util.Tool;

public class APIEventListener implements ApplicationEventListener  {
	private volatile int requestCnt = 0;
	private String requestIdPrefix;
	
	public APIEventListener() {
		try {
			requestIdPrefix = Tool.getSystemIP().getHostName();
		} catch (UnknownHostException e) {
			requestIdPrefix = "api";
		}
		requestIdPrefix = requestIdPrefix+"-"+DateTimeTool.getDateString("yyyyMMddHHmmss", new Date())+"-";
	}

	@Override
	public void onEvent(ApplicationEvent event) {
		switch (event.getType()) {
		case INITIALIZATION_FINISHED:
			System.out.println("Application DataModel API was Initialized.");
			break;
		case DESTROY_FINISHED:
			System.out.println("Application DataModel API destroyed.");
			break;
		default:
			break;
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent requestEvent) {
		requestCnt++;
		System.out.println("Request " +requestIdPrefix+ requestCnt + " started.");
		return new APIRequestEventListener(requestIdPrefix+ requestCnt);
	}
}
