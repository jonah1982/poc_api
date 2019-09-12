package com.soprasteria.modelling.service.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.dao.MongoDocReader;
import com.soprasteria.modelling.framework.db.DBQuery;
import com.soprasteria.modelling.framework.entity.domain.MongoDomain;
import com.soprasteria.modelling.framework.logging.LogDAO;
import com.soprasteria.modelling.framework.util.Tool;
import com.soprasteria.modelling.service.ProjectServiceContext;
import com.soprasteria.modelling.service.notification.NotificationDomain;
import com.soprasteria.modelling.service.notification.util.EmailInfo;
import com.soprasteria.modelling.service.notification.util.MailUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class NotificationSerevice {

    public static String collectionName = "notification";
    protected LogDAO dao;
    protected ProjectServiceContext context;
    private long end;
    private long start;
    public static String STATUS_SUCCESS = "success";
    public static String STATUS_FAIL = "fail";
    public static String STATUS_NEW = "new";


    private int limit = 100;

    public List<NotificationDomain> findAny() {
        final List<NotificationDomain> result = new ArrayList<>();
        dao = new LogDAO();
        MongoDocReader reader = new MongoDocReader() {
            @Override
            public void read(DBObject doc) {
                try {
                    NotificationDomain domain = new NotificationDomain(doc);
                    result.add(domain);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String getCollectionName() {
                return NotificationDomain.class.getAnnotation(Entity.class).name();
            }
        };
        try {
            DBObject doc = new BasicDBObject();
            dao.findLimit(doc, reader, limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<NotificationDomain> quaryNotification(HashMap<String, String> queryMap) {
        dao = new LogDAO();
        final List<NotificationDomain> result = new ArrayList<>();
        DBObject query = new BasicDBObject();
        for (String key : queryMap.keySet()) {
            Object v = queryMap.get(key);
            if (!Tool.isEmpty(v))
                query.put(key, queryMap.get(key));
        }
        MongoDocReader reader = new MongoDocReader() {
            @Override
            public void read(DBObject doc) {
                try {
                    NotificationDomain domain = new NotificationDomain(doc);
                    result.add(domain);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String getCollectionName() {
                return NotificationDomain.class.getAnnotation(Entity.class).name();
            }
        };
        try {
            dao.find(query, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.size() == 0 || result == null) return null;
        return result;
    }

    public List<NotificationDomain> getNotification(long startTime, long endTime) {
        final List<NotificationDomain> result = new ArrayList<>();
        dao = new LogDAO();
        start = startTime;
        end = endTime;
        try {
            MongoDocReader reader = new MongoDocReader() {
                @Override
                public void read(DBObject doc) {
                    try {
                        NotificationDomain domain = new NotificationDomain(doc);
                        result.add(domain);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public String getCollectionName() {
                    return NotificationDomain.class.getAnnotation(Entity.class).name();
                }
            };

            try {
                dao.find(toMongoQuery(), reader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected DBObject toMongoQuery() throws Exception {
        DBObject doc = new BasicDBObject();
        doc.put(MongoDomain.COL_OID,
                DBQuery.toMongoRangeStmt(
                        "" + start,
                        "" + end));
//		context.log("NotificationSearcher, query: "+doc);
        return doc;
    }

    public boolean updateNotification(String toUser, String subject, String content, String mode) throws Exception {
        dao = new LogDAO();
        EmailInfo info = new EmailInfo(toUser, subject, content);
        NotificationDomain noti = new NotificationDomain(toUser, subject, content);
        noti.setTime(new Date());
        noti.setTs(new Date().getTime());
        noti.setSender(info.getFromUserName());
        noti.setMode(mode);
        noti.setStatus(STATUS_NEW);
        dao.set(noti);
        return true;
    }

    public void sendMailQueue() throws Exception {
        dao = new LogDAO();
        HashMap<String, String> query = new HashMap<>();
        query.put("status", STATUS_NEW);
        query.put("mode", "email");
        List<NotificationDomain> list = quaryNotification(query);
        if (list != null && list.size() > 0){
            for (NotificationDomain noti : list) {
                dao.findOne(noti);
                noti.setTime(new Date());
                noti.setTs(new Date().getTime());
                EmailInfo info = new EmailInfo(noti.getRecipient(), noti.getSubject(), noti.getContent());
                MailUtil mail = new MailUtil();
                boolean status = mail.sendHtmlMail(info);
                if (status == true) {
                    noti.setStatus(STATUS_SUCCESS);
                } else {
                    noti.setStatus(STATUS_FAIL);
                }
                dao.update(noti);
            }
        }
    }

    public boolean sendMessagerByMail(String toUser, String subject, String content) throws Exception {
        dao = new LogDAO();
        MailUtil mail = new MailUtil();
        EmailInfo info = new EmailInfo(toUser, subject, content);

        NotificationDomain noti = new NotificationDomain(toUser, subject, content);
        noti.setTime(new Date());
        noti.setTs(new Date().getTime());
        noti.setSender(info.getFromUserName());
        noti.setMode("email");
        boolean status = mail.sendHtmlMail(info);
        if (status == true) {
            noti.setStatus(STATUS_SUCCESS);
        } else {
            noti.setStatus(STATUS_FAIL);
        }
        dao.set(noti);

        return status;
    }

}
