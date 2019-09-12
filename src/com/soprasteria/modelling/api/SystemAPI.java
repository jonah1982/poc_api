package com.soprasteria.modelling.api;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.soprasteria.modelling.framework.config.PropertyFileSetting;
import com.soprasteria.modelling.framework.context.MongoTransaction;
import com.soprasteria.modelling.framework.entity.Location;
import com.soprasteria.modelling.framework.util.Tool;

@Path("/system")
public class SystemAPI extends BaseAPI {
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String defcall() throws Exception {
		throw new Exception("welcome to system api.");
	}
	
	@POST
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getJson(Location json) throws Exception {
		return json.toString();
	}

	@GET
	@Path("/version")
	@Produces({MediaType.APPLICATION_JSON})
	public String getCurrentAPIVersion() throws Exception {
		String version = null;
		try {
			version = PropertyFileSetting.getSetting("version", null);
		} catch (Exception e) {

		}
		if(version == null) version = "beta";

		String result = toSimpleProperJson("version", version);

		return result;
	}

	@GET
	@Path("/info")
	@Produces({MediaType.APPLICATION_JSON})
	public String getCurrentAPIInfo(@Context HttpServletRequest request) throws Exception {
		HashMap<String, Object> apiInfo = new HashMap<>();
		apiInfo.put("context_path", request.getContextPath());
		apiInfo.put("user_agent", request.getHeader("user-agent"));
		apiInfo.put("forwarded", request.getHeader("Forwarded"));
		apiInfo.put("client", request.getRemoteHost());

		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = (String)headerNames.nextElement();
			apiInfo.put("" + headerName, "" + request.getHeader(headerName));
		}

		File settingfile = Tool.getResourceDirectory("setting.properties");
		java.nio.file.Path settingpath = settingfile.toPath();

		BasicFileAttributes view = Files.getFileAttributeView(settingpath, BasicFileAttributeView.class).readAttributes();
		FileTime creationTime = view.creationTime();

		apiInfo.put("deploy_time", creationTime.toString());
		apiInfo.put("deploy_ts", creationTime.toMillis());
		apiInfo.put("server_timezone", TimeZone.getDefault().getID());

		apiInfo.put("setting", PropertyFileSetting.getAllSetting(null));

		return toHashMapJson(apiInfo);
	}

	@GET
	@Path("/dummy")
	@Produces({MediaType.APPLICATION_JSON})
	public String dummyAPI(@QueryParam("result") String result) throws Exception {
		return result;
	}

	@POST
	@Path("/dummy")
	@Produces({MediaType.APPLICATION_JSON})
	public String dummyPostAPI(@FormParam("result") String result) throws Exception {
		return result;
	}


	@GET
	@Path("/mongo")
	@Produces({MediaType.APPLICATION_JSON})
	public String getCurrentMongoInfo() {
		return toHashMapJson(MongoTransaction.getPool());
	}
}
