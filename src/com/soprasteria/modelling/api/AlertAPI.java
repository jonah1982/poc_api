package com.soprasteria.modelling.api;

import com.soprasteria.modelling.service.alert.AlertService;
import com.soprasteria.modelling.service.alert.entity.AlertDomain;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/alert")
public class AlertAPI extends BaseAPI {
    AlertService service = new AlertService();

    @POST
    @Path("/add")
    @Produces({MediaType.APPLICATION_JSON})
    public String addAlert(@QueryParam("token") String accesstoken, @FormParam("alertid") String alertid, @FormParam("type") String type) throws Exception {
        authenAdmin(accesstoken);
        service.addAlert(alertid, type);
        return toSimpleProperJson("message", "success");
    }

    @POST
    @Path("/subscribe")
    @Produces({MediaType.APPLICATION_JSON})
    public String subscrbe(@QueryParam("token") String accesstoken, @FormParam("alertid") String alertid, @FormParam("subscribe") String subscribe) throws Exception {
        authenAdmin(accesstoken);
        service.updateSubscrbe(alertid,subscribe);
        return toSimpleProperJson("message", "success");
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public String findAll(@QueryParam("token") String accesstoken) throws Exception {
        authenAdmin(accesstoken);
        List<AlertDomain> result = service.findAll();
        return toResponse(result);
    }

    @POST
    @Path("/activate")
    @Produces({MediaType.APPLICATION_JSON})
    public String activation(@QueryParam("token") String accesstoken, @FormParam("alertid") String alertid) throws Exception {
        authenAdmin(accesstoken);
        service.activationAlert(alertid);
        return toSimpleProperJson("message", "success");
    }

    @GET
    @Path("/checkverificationcode")
    @Produces({MediaType.APPLICATION_JSON})
    public String checkVerificationCode(@QueryParam("verificationcode") String verificationcode) throws Exception {
        Boolean effective = service.checkVerificationCode(verificationcode);
        if (effective) return toSimpleProperJson("message", "success");
        else throw new Exception("Links have expired");
    }

}
