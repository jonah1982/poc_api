package com.soprasteria.modelling.service.base;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.config.PropertyFileSetting;
import com.soprasteria.modelling.framework.dao.MongoDAO;
import com.soprasteria.modelling.framework.dao.MongoDocReader;
import com.soprasteria.modelling.framework.exception.AuthException;
import com.soprasteria.modelling.framework.util.Tool;
import com.soprasteria.modelling.service.ServiceContext;
import com.soprasteria.modelling.service.base.entity.ProjectDomain;
import com.soprasteria.modelling.service.base.entity.RoleDomain;
import com.soprasteria.modelling.service.base.entity.TokenDomain;
import com.soprasteria.modelling.service.base.entity.UserDomain;
import com.soprasteria.modelling.service.notification.NotificationSerevice;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class UserService {
    public static String collectionName = "user";
    public static String STATUS_IN = "inactive";  // not active
    public static String STATUS_ON = "active";   // active
    public static String STATUS_OUT = "outactive"; //lose efficacy
    private ServiceContext context = new ServiceContext();
    private MongoDAO dao = new MongoDAO(context, context.getBaseMongo());

    public UserDomain authUser(final String username, String password) throws Exception {
        final UserDomain user = new UserDomain(username);
        user.setPassword(encryptPassword(password));

        MongoDocReader reader = new MongoDocReader() {
            @Override
            public void read(DBObject doc) throws Exception {
                UserDomain user2 = new UserDomain(doc);
                if (!user.getPassword().equals(user2.getPassword())) throw new AuthException("Wrong Password.");
                user.merge(doc);
                if (user.getProjects().isEmpty()) throw new AuthException("Invalid user: no project.");
                TokenDomain token = new TokenDomain();
                token.setUserid(username);
                token.setRoleid(user.getRole());
                token.setProjectid(user.getProjects().keySet().toArray()[0].toString());
                user.setToken(token);

            }

            @Override
            public String getCollectionName() {
                return user.getCollectionName();
            }
        };

        dao.findLimit(user.toFindByKeyStmt(), reader, 1);
        if (user.getOid() == null)
            throw new AuthException("Login failed.");

        dao.update(user.getToken());
        //	updateStatus(user.getToken().getToken());
        return user;
    }

    public UserDomain getUser(String username) throws Exception {
        UserDomain user = new UserDomain(username);
        dao.findOne(user);
        return user;
    }

    public void addUser(String username, String password, String role, String projectid, String email) throws Exception {
        UserDomain user = new UserDomain(username);
        if (dao.exist(user)) throw new Exception("用户名： " + user.getUsername() + " 已存在");
        if (password == null || password.isEmpty()) throw new Exception("Password can not be blank.");
        user.setPassword(encryptPassword(password));
        user.setRole(role);
        ProjectService pservice = new ProjectService();
        ProjectDomain project = pservice.getProject(projectid);
        HashMap<String, String> projects = new HashMap<>();
        projects.put(project.getOid().toString(), project.getName());
        user.setProjects(projects);
        user.setEmail(email);
        user.setStatus(STATUS_ON);
        dao.set(user);
    }

    public void addUserByNotification(String username, String phone, String email) throws Exception {
        //validateRole(role);
        //if(phone == null && email==null) throw new Exception("phone and email is null") ;
        if (email == null) throw new Exception("email is null");
        if (!reg(phone, email)) throw new Exception("phone /email style error");
        if (isExit(phone, email)) throw new Exception("phone or email exists");
//		String password = "";//get password


        UserDomain user = new UserDomain(username);
        user.setPhone(phone);
        user.setEmail(email);
        user.setStatus(STATUS_IN);

        user.setVerificationcode(UUID.randomUUID().toString().replaceAll("-", ""));

        user.setRole("admin");
        ProjectService pservice = new ProjectService();
        user.setProjects(pservice.getAllProjectName());

        if (dao.exist(user)) throw new Exception("User " + username + " already exists.");
        boolean result = notification(email, user.getVerificationcode());
        if (!result) throw new Exception("notification error");
        dao.update(user);
    }

    public boolean notification(String email, String verificationcode) throws Exception {
        //String toUser = "zhengfei@mathearth.com";
        String toUser = email;
        String subject = "上海邦芯账户激活";
        String URL = "https://smartwater.mathearth.com/smartwater/#!/verification?verificationcode=" + verificationcode;
        String content = "您好，您的邦芯账户已创建:请点击<a href='" + URL + "'>" + URL + "</a>" + "进行激活(24小时内有效)";
        NotificationSerevice noti = new NotificationSerevice();
        boolean result = noti.sendMessagerByMail(toUser, subject, content);
        return result;
    }

    public String getMathPassword() {
        return "";
    }

    public boolean reg(String phone, String email) throws Exception {
        boolean phoneReg = true;
        boolean emailReg = true;
        if (phone != null) {
            //verify/phone
            //String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,5-9])|(177))\\d{8}$";
            String regex = "^1[3|4|5|6|7|8][0-9]\\d{8}$";
            phoneReg = phone.matches(regex);
        }

        if (email != null) {
            //verify/email
            String repx = "^[A-Za-z0-9]+([._\\\\-]*[A-Za-z0-9])*@([A-Za-z0-9]+.){1,63}[A-Za-z0-9]+$";
            emailReg = email.matches(repx);
        }
        if (phoneReg && emailReg) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isExit(String phone, String email) throws Exception {
        UserDomain user1;
        UserDomain user2;
        if (phone != null) {
            HashMap<String, String> queryMap2 = new HashMap<>();
            queryMap2.put("phone", phone);
            user1 = queryUser(queryMap2);
        } else {
            user1 = null;
        }
        if (email != null) {
            HashMap<String, String> queryMap = new HashMap<>();
            queryMap.put("email", email);
            user2 = queryUser(queryMap);
        } else {
            user2 = null;
        }
        if (user1 != null || user2 != null) {
            return true;
        } else {
            return false;
        }
    }

    public void updateStatus(String token) throws Exception {
        TokenDomain tokendomain = new TokenDomain(token);
        dao.findOne(tokendomain);
        if (tokendomain.isValid()) {
            String username = tokendomain.getUserid();
            UserDomain user = new UserDomain(username);
            dao.findOne(user);

            if (user.getOid() == null) throw new Exception("No User assigned.");

            dao.set(tokendomain);
            user.setStatus(STATUS_ON);
            user.setToken(tokendomain);
        }

    }

    public void updateUser(UserDomain user) throws Exception {
        if (!dao.exist(user)) throw new Exception("User " + user + " doesnt exist.");
        dao.set(user);
    }

    public UserDomain validateToken(String token) throws Exception {
        TokenDomain tokendomain = new TokenDomain(token);
        dao.findOne(tokendomain);
        if (tokendomain.isValid()) {
            String username = tokendomain.getUserid();
            UserDomain user = new UserDomain(username);
            dao.findOne(user);

            if (user.getOid() == null) throw new Exception("No User assigned.");

            dao.set(tokendomain);

            user.setToken(tokendomain);
            return user;
        } else {
            throw new AuthException("Invalid Token.");
        }
    }

    public UserDomain validateToken(String token, String projectId) throws Exception {
        TokenDomain tokendomain = new TokenDomain(token);
        dao.findOne(tokendomain);
        if (tokendomain.isValid()) {
            String username = tokendomain.getUserid();
            UserDomain user = new UserDomain(username);
            dao.findOne(user);

            if (user.getOid() == null) throw new AuthException("No User assigned.");
            if (!user.getProjects().containsKey(projectId))
                throw new AuthException("User not authorized for this project :" + projectId);

            tokendomain.setProjectid(projectId);
            tokendomain.setRoleid(user.getRole());
            dao.set(tokendomain);

            user.setToken(tokendomain);
            return user;
        } else {
            throw new AuthException("Invalid Token.");
        }
    }

    public UserDomain queryUser(HashMap<String, String> queryMap) throws Exception {
        final List<UserDomain> list = new ArrayList<>();
		/*TokenDomain tokendomain = new TokenDomain(token);
		dao.findOne(tokendomain);
		if(tokendomain.isValid()) {	} else {throw new AuthException("Invalid Token.");}*/

        DBObject query = new BasicDBObject();
        for (String key : queryMap.keySet()) {
            Object v = queryMap.get(key);
            if (!Tool.isEmpty(v))
                query.put(key, queryMap.get(key));
        }
        MongoDocReader reader = new MongoDocReader() {

            @Override
            public void read(DBObject doc) throws Exception {
                UserDomain user = new UserDomain(doc);
                list.add(user);
            }

            @Override
            public String getCollectionName() {
                return collectionName;
            }
        };
        try {
            dao.find(query, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.size() == 0 || list == null) return null;
        return list.get(0);
    }

    public List<UserDomain> queryAllUser(HashMap<String, String> queryMap) throws Exception {
        final List<UserDomain> list = new ArrayList<>();


        DBObject query = new BasicDBObject();
        for (String key : queryMap.keySet()) {
            Object v = queryMap.get(key);
            if (!Tool.isEmpty(v))
                query.put(key, queryMap.get(key));
        }
        MongoDocReader reader = new MongoDocReader() {

            @Override
            public void read(DBObject doc) throws Exception {
                UserDomain user = new UserDomain(doc);
                list.add(user);
            }

            @Override
            public String getCollectionName() {
                return collectionName;
            }
        };
        try {
            dao.find(query, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<UserDomain> queryAllUserByProjectId(String project, String role) throws Exception {
        final List<UserDomain> list = new ArrayList<>();
        final String rolename = role;
        DBObject query = new BasicDBObject();
        query.put("projects." + project, new BasicDBObject().append("$exists", true));
        MongoDocReader reader = new MongoDocReader() {

            @Override
            public void read(DBObject doc) throws Exception {
                UserDomain user = new UserDomain(doc);
                if (rolename.equals("superadmin")) list.add(user);
                else if (!rolename.equals("superadmin") && !user.getRole().equals("superadmin")) list.add(user);
            }

            @Override
            public String getCollectionName() {
                return collectionName;
            }
        };
        try {
            dao.find(query, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public void assignRole(String username, String role) throws Exception {
        validateRole(role);

        UserDomain user = new UserDomain(username);

        if (!dao.exist(user)) throw new Exception("User " + username + " doesnt exist, please create user first.");

        dao.findOne(user);
        user.setRole(role);

        dao.set(user);
    }

    public void validateProject(String projectId) throws Exception {
        ProjectDomain project = new ProjectDomain(projectId);
        if (!dao.exist(project)) throw new Exception("Invalid project id " + projectId);
    }

    public void validateRole(String roleId) throws Exception {
        RoleDomain role = new RoleDomain(roleId);
        if (!dao.exist(role)) throw new Exception("Invalid role " + roleId);
    }

    public List<ProjectDomain> listAllProjects() throws Exception {
        final List<ProjectDomain> projlist = new ArrayList<>();
        dao.findAll(new MongoDocReader() {
            @Override
            public void read(DBObject doc) throws Exception {
                projlist.add(new ProjectDomain(doc));
            }

            @Override
            public String getCollectionName() {
                return ProjectDomain.class.getAnnotation(Entity.class).name();
            }
        });

        return projlist;
    }

    public List<RoleDomain> listRoles() throws Exception {
        final List<RoleDomain> rolelist = new ArrayList<>();
        dao.findAll(new MongoDocReader() {

            @Override
            public void read(DBObject doc) throws Exception {
                rolelist.add(new RoleDomain(doc));
            }

            @Override
            public String getCollectionName() {
                return RoleDomain.class.getAnnotation(Entity.class).name();
            }
        });

        return rolelist;
    }

    public RoleDomain getRole(String roleId) throws Exception {
        RoleDomain roleDomain = new RoleDomain(roleId);
        dao.findOne(roleDomain);

        return roleDomain;
    }

    public boolean checkVerificationCode(String verificationcode) throws Exception {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("verificationcode", verificationcode);
        UserDomain user = queryUser(queryMap);
        if (new Date().getTime() - user.getLastmod().getTime() < 1000 * 60 * 60 * 24) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUsernameByVerificationCode(String username, String verificationcode) throws Exception {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("verificationcode", verificationcode);
        UserDomain user = queryUser(queryMap);
        if (user.getUsername().equals(username)) {
            return true;
        } else {
            return false;
        }
    }

//    public void resetPassword(String username, String verificationcode, String password) throws Exception {
//        HashMap<String, String> queryMap = new HashMap<>();
//        queryMap.put("username", username);
//        UserDomain user = queryUser(queryMap);
//        user.setPassword(encryptPassword(password));
//        user.setStatus(status_on);
//        dao.set(user);
//    }

    public void resetPassword(String username, String oldpassword, String password) throws Exception {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("username", username);
        UserDomain user = queryUser(queryMap);
        if (!encryptPassword(oldpassword).equals(user.getPassword())) throw new Exception("Error Old Password ");
        user.setPassword(encryptPassword(password));
        user.setStatus(STATUS_ON);
        dao.set(user);
    }

    public void updatePassword(String username, String password) throws Exception {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("username", username);
        UserDomain user = queryUser(queryMap);
        user.setPassword(encryptPassword(password));
        dao.set(user);
    }

    public void lockUser(String username) throws Exception {
        UserDomain user = getUser(username);
        user.setStatus(STATUS_IN);
        updateUser(user);
    }

    public void unlockUser(String username) throws Exception {
        UserDomain user = getUser(username);
        user.setStatus(STATUS_ON);
        updateUser(user);
    }

    public ProjectDomain getProject(String projectId) throws Exception {
        ProjectDomain project = new ProjectDomain(projectId);
        dao.findOne(project);
        return project;
    }

    public void createRole(String roleId, List<String> apps) throws Exception {
        RoleDomain role = new RoleDomain(roleId);
        role.setApps(apps);
        dao.set(role);
    }

    public static String encryptPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("md5");

        String pre = PropertyFileSetting.getSetting("encryption.pre", null);
        String post = PropertyFileSetting.getSetting("encryption.post", null);

        md.update(pre.getBytes());
        md.update(password.getBytes());
        md.update(post.getBytes());

        byte[] byteData = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
