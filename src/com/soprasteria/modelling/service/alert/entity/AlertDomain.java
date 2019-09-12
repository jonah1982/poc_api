package com.soprasteria.modelling.service.alert.entity;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.mongodb.DBObject;

import java.util.HashMap;

@Entity(name = "alertmgmt")
public class AlertDomain extends MongoAnnoDomain {

    @Column
    private String status;

    @Column
    private HashMap<String, Object> subscribe;

    @Column
    private String verificationcode;

    @Column
    private String type;

    public AlertDomain(String mail) {
        setOid(mail.toString());
    }

    public AlertDomain(DBObject doc) throws Exception {
        super(doc);
    }

    public void setSubscribe(HashMap<String, Object> subscribe) {
        this.subscribe = subscribe;
    }


    public HashMap<String, Object> getSubscribe() {
        return subscribe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerificationcode() {
        return verificationcode;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
