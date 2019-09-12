package com.soprasteria.modelling.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.soprasteria.modelling.service.notification.NotificationSerevice;

@Path("/notification")
public class NotificationAPI extends BaseAPI {
	@GET
	@Path("/search/{start}/{end}")
	public String getNotificationByTime(@QueryParam("token") String accesstoken,@PathParam("start") Long start, @PathParam("end") Long end) throws Exception {
		authenAdmin(accesstoken);
		NotificationSerevice bean = new NotificationSerevice();
		return toResponse(bean.getNotification(start, end));
	}
	
	@GET
	@Path("/search/any")
	public String getNotificationAny(@QueryParam("token") String accesstoken) throws Exception {
		authenAdmin(accesstoken);
		NotificationSerevice bean = new NotificationSerevice();
		return toResponse(bean.findAny());
	}
}
