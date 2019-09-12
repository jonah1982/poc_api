package com.soprasteria.modelling.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.soprasteria.modelling.api.entity.LangEntity;
import com.soprasteria.modelling.service.lang.BingTranslatorSoapManager;
import com.soprasteria.modelling.service.lang.LangService;

@Path("/lang")
public class LangAPI extends BaseAPI {
	@GET
	@Path("/convert/{context}/{termid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String dummyAPI(@Context HttpServletRequest request, @PathParam("context") String langContext, @PathParam("termid") String termId, 
			@QueryParam("token") String accesstoken, @QueryParam("lang") String lang) throws Exception {
		authen(accesstoken);
		LangEntity langentity = new LangEntity(langContext, termId.toLowerCase(), termId);
		if(lang != null) translate(langentity, lang);
		else translate(langentity, context.getUser().getLang());
		return toResponse(langentity);
	}
	
	private void translate(LangEntity entity, String lang) throws Exception {
		if(lang == null) return;
		LangService service = new LangService(context.toServiceContext());
		String text = service.translate(entity.getContext(), entity.getOrigin(), lang);
		
		if(text != null) {
			entity.setTarget(lang, text);
			return;
		}
		
		// else use Bing translate
		context.log("no translation available in db, use Bing to translate.");
		if(!BingTranslatorSoapManager.isValid(context, lang)) throw new Exception("Invalid language code "+lang);
		
		try {
			entity.setTarget(lang, BingTranslatorSoapManager.autoTranslate(context, entity.getOrigin(), lang));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
