package com.soprasteria.modelling.api.auth;

import java.util.UUID;

import com.soprasteria.modelling.framework.entity.domain.MongoDomain;

public class AccessToken extends MongoDomain {
	
	public static String collectionName = "access_token";
	
	public static String COL_ID = "id";
	public static String COL_USERID = "user_id";
	public static String COL_TOKEN = "token";
	public static String COL_CLIENTID = "client_id";
	public static String COL_APPID = "app_id";
	public static String COL_ACCESSDATA = "access_data";
	public static String COL_CREATED = "created";
	public static String COL_LASTMOD = "last_mod";
	public static String COL_LOCALE = "locale";
	
	public AccessToken(Long userid, Long appId, String clientId, String locale, String accessdata) {
		super(collectionName);
		setField(COL_USERID, userid);
		setField(COL_APPID, appId);
		setField(COL_CLIENTID, clientId);
		setField(COL_LOCALE, locale);
		setField(COL_ACCESSDATA, accessdata);
		setField(COL_TOKEN, UUID.randomUUID().toString());
	}
	
	public String getToken() {
		return getStringField(COL_TOKEN);
	}
}
