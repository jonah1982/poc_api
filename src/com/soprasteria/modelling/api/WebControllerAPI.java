package com.soprasteria.modelling.api;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.soprasteria.modelling.framework.util.Tool;
import com.soprasteria.modelling.framework.util.URLGenerator;

@Path("/webcontrol")
public class WebControllerAPI extends BaseAPI {
	@GET
	public String index() {
		return "<a href='http://localhost:8080/api/webcontrol/sensor?sensortype=PRESSURE'>go to sensor</a>";
	}

	@GET
	@Path("/target")
	@Produces({MediaType.APPLICATION_JSON})
	public String target(@Context HttpServletRequest request) {
		return new Gson().toJson(request);
	}

	@GET
	@Path("/sensorplot")
	@Produces({MediaType.APPLICATION_JSON})
	public Response redirectSensorPlot(@QueryParam("station") String station,
			@QueryParam("zone") String zone, 
			@QueryParam("sensortype") String sensortype, 
			@QueryParam("userid") String userid, 
			@QueryParam("targeturl") String targetURL,
			@Context HttpServletRequest request) throws URISyntaxException, MalformedURLException {
		URL baseurl = getTargetURL(targetURL, request, "/appz/m/m-plot.php");
		URLGenerator urlgenerator = new URLGenerator(baseurl);
		
//		JServiceContext jservicecontext = context.genJServiceContext(zone);
		String stationName = "";
//		StationDAO stationdao = new StationDAO(jservicecontext, jservicecontext.getDataTrans());
//		
//		String stationName = stationdao.getStationName(station);
		
//		if(userid != null && userid.startsWith("pub_") && sensortype.equalsIgnoreCase("pressure")) stationName = stationName+" - Elev: "+stationdao.getStationElevation(station);
		
		urlgenerator.setParameter("nodeid", station);
		urlgenerator.setParameter("nodename", stationName);
		urlgenerator.setParameter("sensortype", sensortype);
		urlgenerator.setParameter("querysource", "sensordata");
		urlgenerator.setParameter("period", 24);
		urlgenerator.setParameter("systemname", zone);
		
		URI location = urlgenerator.getUrl().toURI();
		context.log("redirecting to "+location);
		return Response.temporaryRedirect(location).build();
	}
	
	private URL getTargetURL(String targetURL, HttpServletRequest request, String targetPath) throws MalformedURLException {
		if(Tool.isEmpty(targetURL)) targetURL = request.getHeader("referer");
		
		URL url = new URL(targetURL);
		String path = url.getPath().substring(0, url.getPath().indexOf("/", 1));
		URL target = new URL(url.getProtocol(), url.getHost(), url.getPort(),path+targetPath);
		return target;
	}
}
