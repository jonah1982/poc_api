package com.soprasteria.modelling.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.soprasteria.modelling.framework.config.PropertyFileSetting;
import com.soprasteria.modelling.framework.util.Tool;
import com.soprasteria.modelling.service.ProjectServiceContext;

@Path("/report")
public class ReportAPI extends BaseAPI {

	@GET
	@Path("/ns/dirs")
	@Produces({MediaType.APPLICATION_JSON})
	public String listNSDirs(@QueryParam("token") String accesstoken) throws Exception {
		authen(accesstoken);
		
		File projectReportDir = getReportRootDir(context.toProjectContext());
		
		List<HashMap<String, String>> result = new ArrayList<>();
		
		File[] flist = projectReportDir.listFiles();
		for(File f: flist) {
			if(f.isDirectory()) {
				HashMap<String, String> fobject = new HashMap<>();
				String name = f.getName();
				fobject.put("name", name);
				fobject.put("id", getFolderId(name));
				result.add(fobject);
			}
		}
		
		return toResponse(result);
	}
	
	@GET
	@Path("/ns/dir/{dirid}")
	@Produces({MediaType.APPLICATION_JSON})
	public String listNSDirFiles(@QueryParam("token") String accesstoken, @PathParam("dirid") String dirid) throws Exception {
		authen(accesstoken);
		
		File folder = getFolderPath(context.toProjectContext(), dirid);
		
		List<HashMap<String, String>> result = new ArrayList<>();
		
		File[] flist = folder.listFiles();
		for(File f: flist) {
			if(f.isFile()) {
				HashMap<String, String> fobject = new HashMap<>();
				String name = f.getName();
				fobject.put("name", name);
				fobject.put("id", getFileId(folder.getName(), name));
				result.add(fobject);
			}
		}
		
		return toResponse(result);
	}
	
	@GET
	@Path("/ns/file/{fileid}")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response downloadFile(@QueryParam("token") String accesstoken, @PathParam("fileid") String fileid) throws Exception {
		authen(accesstoken);
		
		File file = getFilePath(context.toProjectContext(), fileid);
		ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes(),"ISO8859-1"));
		
        return response.build();
	}
	
	private File getReportRootDir(ProjectServiceContext context) throws Exception {
		File projectReportDir = null;
		try {
			String reportDir = PropertyFileSetting.getSetting("fs.report", null);
			projectReportDir = new File(reportDir, context.getProject().getOid().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Fail to load report root dir due to: "+e.getMessage());
		}
		return projectReportDir;
	}
	
	private String getFolderId(String folderName) throws UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString(folderName.getBytes("UTF-8"));
	}
	
	private String getFileId(String folderName, String fileName) throws UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString((folderName+"<|>"+fileName).getBytes("UTF-8"));
	}
	
	private File getFolderPath(ProjectServiceContext context, String folderId) throws Exception {
		String folderName = new String(Base64.getDecoder().decode(folderId),"UTF-8");
		if(!Tool.isNormalString(folderName)) throw new Exception("Invalid Folder Name: "+folderName);
		return new File(getReportRootDir(context), folderName);
	}
	
	private File getFilePath(ProjectServiceContext context, String fileId) throws Exception {
		String fullName = new String(Base64.getDecoder().decode(fileId),"UTF-8");
		String dn, fn = null;
		
		try {
			StringTokenizer st = new StringTokenizer(fullName, "<|>");
			dn = st.nextToken();
			fn = st.nextToken();
			if(!Tool.isNormalString(dn)) throw new Exception("Invalid Folder Name: "+dn);
			if(!Tool.isNormalString(fn)) throw new Exception("Invalid File Name: "+fn);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Fail to derive folder name and file name from "+fullName+" due to "+e.getMessage());
		}
		
		return new File(new File(getReportRootDir(context), dn), fn);
	}
}
