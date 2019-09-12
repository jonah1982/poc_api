package com.soprasteria.modelling.api;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.soprasteria.modelling.service.base.UserService;
import com.soprasteria.modelling.service.base.entity.RoleDomain;
import com.soprasteria.modelling.service.base.entity.UserDomain;

@Path("/user")
public class UserAPI extends BaseAPI {
    private UserService userService = new UserService();

    @GET
    @Path("/me")
    @Produces({MediaType.APPLICATION_JSON})
    public String getProfile(@QueryParam("token") String accesstoken) throws Exception {
        authenNormalUser(accesstoken);

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", context.getUser().getUsername());
        result.put("lang", context.getUser().getLang());
        result.put("timezone", context.getUser().getTimezone());
        result.put("role", context.getUser().getRole());
        result.put("apps", RoleDomain.ALL_APPS);
        result.put("token", context.getUser().getToken().getToken());
        result.put("projects", context.getUser().getProjects());
        result.put("current_project", userService.getProject(context.getUser().getToken().getProjectid()));
        result.put("access", context.getUser().getAccess());

        return toHashMapJson(result);
    }

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String login(@FormParam("username") String userName, @FormParam("password") String password) throws Exception {
        userName = userName.toLowerCase();
        UserDomain user = authmanager.authenUser(userName, password);
        if (user.getStatus().equals(UserService.STATUS_IN)) throw new Exception("User " +userName + "not activated");
        context.setUser(user);

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", context.getUser().getUsername());
        result.put("lang", context.getUser().getLang());
        result.put("timezone", context.getUser().getTimezone());
        result.put("role", context.getUser().getRole());
        result.put("apps", RoleDomain.ALL_APPS);
        result.put("token", context.getUser().getToken().getToken());
        result.put("projects", context.getUser().getProjects());
        result.put("current_project", userService.getProject(context.getUser().getToken().getProjectid()));
        result.put("access", context.getUser().getAccess());

        return toHashMapJson(result);
    }

//	@POST
//	@Path("/login")
//	@Produces({MediaType.APPLICATION_JSON})
//	@Consumes({MediaType.APPLICATION_JSON})
//	public String login(String credential) throws Exception {
//		context.log("receive credential: "+ credential);
//		String userName = credential.getUserName().toLowerCase();
//		String password = credential.getPassword();
//		UserDomain user = authmanager.authenUser(userName, password);
//		context.setUser(user);
//		
//		HashMap<String, Object> result = new HashMap<>();
//		result.put("username", user.getUsername());
//		result.put("lang", context.getUser().getLang());
//		result.put("timezone", context.getUser().getTimezone());
//		result.put("role", user.getRole());
//		result.put("apps", RoleDomain.ALL_APPS);
//		result.put("token", user.getToken().getToken());
//		result.put("projects", user.getProjects());
//		result.put("current_project", userService.getProject(context.getUser().getToken().getProjectid()));
//		
//		return toHashMapJson(result);
//	}

    @POST
    @Path("/switchproject")
    @Produces({MediaType.APPLICATION_JSON})
    public String switchProject(@FormParam("token") String accesstoken, @FormParam("projectid") String projectid) throws Exception {
        changeContext(accesstoken, projectid);
        String roleid = context.getUser().getRole();
        if (roleid == null) throw new Exception("User doesnt have access to this customer.");

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", context.getUser().getUsername());
        result.put("lang", context.getUser().getLang());
        result.put("timezone", context.getUser().getTimezone());
        result.put("role", context.getUser().getRole());
        result.put("apps", RoleDomain.ALL_APPS);
        result.put("token", context.getUser().getToken().getToken());
        result.put("projects", context.getUser().getProjects());
        result.put("current_project", userService.getProject(context.getUser().getToken().getProjectid()));

        return toHashMapJson(result);
    }

    @GET
    @Path("/verify/phone")
    @Produces({MediaType.APPLICATION_JSON})
    public String getUserByPhone(@QueryParam("token") String accesstoken, @QueryParam("phone") String phone) throws Exception {
        authenAdmin(accesstoken);
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("phone", phone);
        UserDomain userDomain = userService.queryUser(queryMap);
        if (userDomain != null) {
            return toSimpleProperJson("message", "exit");
        } else {
            return toSimpleProperJson("message", "noexit");
        }

    }

    @GET
    @Path("/verify/email")
    @Produces({MediaType.APPLICATION_JSON})
    public String getUserByemail(@QueryParam("token") String accesstoken, @QueryParam("email") String email) throws Exception {
        authenAdmin(accesstoken);
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("email", email);
        UserDomain userDomain = userService.queryUser(queryMap);
        if (userDomain != null) {
            return toSimpleProperJson("message", "exit");
        } else {
            return toSimpleProperJson("message", "noexit");
        }
    }


    @POST
    @Path("/adduser")
    @Produces({MediaType.APPLICATION_JSON})
    public String addUser(@QueryParam("token") String accesstoken, @FormParam("username") String username, @FormParam("password") String password, @FormParam("role") String role, @FormParam("email") String email) throws Exception {
        authenAdmin(accesstoken);
        //HashMap<String,String> project =formProject(projects);
        context.log("user" + username + " role" + role);
        if (context.getUser().getRole().equals("admin") && role.equals("superadmin")) throw new Exception("Permissions could not be created superadmin");
        userService.addUser(username, password, role, context.getUser().getToken().getProjectid(), email);
        return toSimpleProperJson("message", "success");
    }

    @POST
    @Path("/updateuser")
    @Produces({MediaType.APPLICATION_JSON})
    public String updateUser(@QueryParam("token") String accesstoken, @FormParam("username") String username, @FormParam("email") String email, @FormParam("role") String role) throws Exception {
        authenAdmin(accesstoken);
        List<UserDomain> userList = userService.queryAllUserByProjectId(context.getUser().getToken().getProjectid(), context.getUser().getRole());
        for (UserDomain u : userList) {
            if (u.getUsername().equals(username)) {
                UserDomain user = userService.getUser(username);
                user.setRole(role);
                user.setEmail(email);
                userService.updateUser(user);
            }
        }

        return toSimpleProperJson("message", "success");
    }

    @GET
    @Path("/alluser")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllUser(@QueryParam("token") String accesstoken) throws Exception {
        authenSuperAdmin(accesstoken);
        HashMap<String, String> query = new HashMap<>();
        List<UserDomain> result = userService.queryAllUser(query);
        return toResponse(result);
    }

    @GET
    @Path("/project/alluser")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllUserByProjectId(@QueryParam("token") String accesstoken) throws Exception {
        authenAdmin(accesstoken);
        List<UserDomain> result = userService.queryAllUserByProjectId(context.getUser().getToken().getProjectid(), context.getUser().getRole());
        return toResponse(result);
    }

    @GET
    @Path("/allrole")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllRole(@QueryParam("token") String accesstoken) throws Exception {
        authenAdmin(accesstoken);
        List<RoleDomain> result = userService.listRoles();
        return toResponse(result);
    }


    @POST
    @Path("/resetpassword")
    @Produces({MediaType.APPLICATION_JSON})
    public String resetPassword(@QueryParam("token") String accesstoken, @FormParam("oldpassword") String oldpassword, @FormParam("password") String password) throws Exception {
        authenNormalUser(accesstoken);
        userService.resetPassword(context.getUser().getUsername(), oldpassword, password);
        return toSimpleProperJson("message", "success");
    }


    @POST
    @Path("/updatepassword")
    @Produces({MediaType.APPLICATION_JSON})
    public String updatePassword(@QueryParam("token") String accesstoken, @FormParam("username") String username, @FormParam("password") String password) throws Exception {
        authenAdmin(accesstoken);
        List<UserDomain> userList = userService.queryAllUserByProjectId(context.getUser().getToken().getProjectid(), context.getUser().getRole());
        for (UserDomain u : userList) {
            if (u.getUsername().equals(username)) {
                userService.updatePassword(username, password);
                return toSimpleProperJson("message", "success");
            }
        }
        return toSimpleProperJson("message", "update password fail");

    }

    @GET
    @Path("/lockuser")
    @Produces({MediaType.APPLICATION_JSON})
    public String lockUser(@QueryParam("token") String accesstoken, @QueryParam("username") String username) throws Exception {
        authenAdmin(accesstoken);
        List<UserDomain> userList = userService.queryAllUserByProjectId(context.getUser().getToken().getProjectid(), context.getUser().getRole());
        for (UserDomain u : userList) {
            if (u.getUsername().equals(username)) {
                userService.lockUser(username);
                return toSimpleProperJson("message", "success");
            }
        }
        return toSimpleProperJson("message", "lock user fail");
    }

    @GET
    @Path("/unlockuser")
    @Produces({MediaType.APPLICATION_JSON})
    public String unlockUser(@QueryParam("token") String accesstoken, @QueryParam("username") String username) throws Exception {
        authenAdmin(accesstoken);
        List<UserDomain> userList = userService.queryAllUserByProjectId(context.getUser().getToken().getProjectid(), context.getUser().getRole());
        for (UserDomain u : userList) {
            if (u.getUsername().equals(username)) {
                userService.unlockUser(username);
                return toSimpleProperJson("message", "success");
            }
        }
        return toSimpleProperJson("message", "unlock user fail");
    }

    @GET
    @Path("/checkverificationcode")
    @Produces({MediaType.APPLICATION_JSON})
    public String checkVerificationCode(@QueryParam("verificationcode") String verificationcode) throws Exception {
        Boolean effective = userService.checkVerificationCode(verificationcode);
        if (effective) {
            return toSimpleProperJson("message", "success");
        } else {
            throw new Exception("Links have expired");
        }

    }

    @GET
    @Path("/checkusername")
    @Produces({MediaType.APPLICATION_JSON})
    public String checkUsername(@QueryParam("username") String username, @QueryParam("verificationcode") String verificationcode) throws Exception {
        Boolean effective = userService.checkVerificationCode(verificationcode);
        if (!effective) {
            throw new Exception("Links have expired");
        }
        if (!userService.checkUsernameByVerificationCode(username, verificationcode)) {
            throw new Exception("invalid username");
        }
        return toSimpleProperJson("message", "success");
    }


    @POST
    @Path("/initializepassword")
    @Produces({MediaType.APPLICATION_JSON})
    public String initializePassword(@FormParam("username") String username, @FormParam("verificationcode") String verificationcode, @FormParam("password") String password) throws Exception {
        Boolean effective = userService.checkVerificationCode(verificationcode);
        if (!effective) {
            throw new Exception("Links have expired");
        }
        if (!userService.checkUsernameByVerificationCode(username, verificationcode)) {
            throw new Exception("invalid username");
        }
        try {
            userService.resetPassword(username, verificationcode, password);
        } catch (Exception e) {
            throw new Exception("reset password fail");
        }
        return toSimpleProperJson("message", "success");
    }


}
