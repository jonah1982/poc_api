package com.soprasteria.modelling.service.alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.dao.MongoDAO;
import com.soprasteria.modelling.framework.dao.MongoDocReader;
import com.soprasteria.modelling.framework.util.Tool;
import com.soprasteria.modelling.service.ServiceContext;
import com.soprasteria.modelling.service.alert.entity.AlertDomain;
import com.soprasteria.modelling.service.notification.NotificationSerevice;


public class AlertService {
    public static String collectionName = "alertmgmt";
    public static String STATUS_IN = "inactive";  // not active
    public static String STATUS_ON = "active";   // active
    public static String TYPE_MAIL = "email";
    public static String TYPE_PHONE = "phone";
    private ServiceContext context = new ServiceContext();
    private MongoDAO dao = new MongoDAO(context, context.getBaseMongo());

    public void addAlert(String alertid, String type) throws Exception {
        alertid = alertid.replaceAll(" ","");
        if (type.equals(TYPE_MAIL)){
            if (!reg(null, alertid)) {
                throw new Exception("Email address format error");
            }
        }
        else if (type.equals(TYPE_PHONE)){
            if (!reg(alertid, null)) {
                throw new Exception("Wrong format of phone number");
            }
        }
        else throw new Exception("type error");
        AlertDomain alert = new AlertDomain(alertid);
        if (exist(alert)) throw new Exception("alert is existed");
        alert.setType(type);
        alert.setStatus(STATUS_IN);
        alert.setVerificationcode(UUID.randomUUID().toString().replaceAll("-", ""));
        dao.update(alert);
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

    public AlertDomain getAlert(String alertid) throws Exception {
        AlertDomain domain = new AlertDomain(alertid);
        dao.findOne(domain);
        return domain;
    }

    public void updateSubscrbe(String alertid, String subscribe) throws Exception {
        AlertDomain domain = new AlertDomain(alertid);
        HashMap<String,Object> newsubscrbe = new HashMap<>();
        String[] subs =  subscribe.split(",");
        for (String sub :subs){
            newsubscrbe.put(sub,"1");
        }
        domain.setSubscribe(newsubscrbe);
        dao.set(domain);
    }

    public List<AlertDomain> findAll() {
        HashMap<String, String> query = new HashMap<>();
        return queryAlert(query);
    }

    public List<AlertDomain> queryAlert(HashMap<String, String> queryMap) {
        final List<AlertDomain> list = new ArrayList<>();
        DBObject query = new BasicDBObject();
        for (String key : queryMap.keySet()) {
            Object v = queryMap.get(key);
            if (!Tool.isEmpty(v))
                query.put(key, queryMap.get(key));
        }
        MongoDocReader reader = new MongoDocReader() {

            @Override
            public void read(DBObject doc) throws Exception {
                AlertDomain Alert = new AlertDomain(doc);
                list.add(Alert);
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
        return list;
    }

    public boolean exist(AlertDomain alertDomain) throws Exception {
        return dao.exist(alertDomain);
    }

    public void activationAlert(String alertid) throws Exception {
        AlertDomain domain = getAlert(alertid);
        if (domain == null) throw new Exception("error alertid");
        if (domain.getType().equals(TYPE_MAIL)) {
            Boolean status = notification(domain.getOid().toString(), domain.getVerificationcode());
            if (!status) throw new Exception("Notification failure");
        }else if (domain.getType().equals(TYPE_PHONE)){

        } else throw new Exception("No type to use");
        dao.set(domain);
    }

    public boolean notification(String email, String verificationcode) throws Exception {
        String subject = "subject";
        String URL = "https://url/#!/verification?verificationcode=" + verificationcode;
        String content = "Hello <a href='" + URL + "'>" + URL + "</a>";
        NotificationSerevice noti = new NotificationSerevice();
        boolean result = noti.sendMessagerByMail(email, subject, content);
        return result;
    }

    public Boolean checkVerificationCode(String verificationcode) throws Exception {
        HashMap<String, String> queryMap = new HashMap<>();
        AlertDomain domain;
        queryMap.put("verificationcode", verificationcode);
        try{
            domain = queryAlert(queryMap).get(0);
        }catch (Exception e){
            throw new Exception("verificationCode error");
        }
        if (new Date().getTime() - domain.getLastmod().getTime() < 1000 * 60 * 60 * 24) {
            domain.setStatus(STATUS_ON);
            dao.set(domain);
            return true;
        } else return false;
    }
}
