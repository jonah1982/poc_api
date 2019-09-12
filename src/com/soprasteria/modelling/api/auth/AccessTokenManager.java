package com.soprasteria.modelling.api.auth;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import com.soprasteria.modelling.api.exception.APITokenException;
import com.soprasteria.modelling.framework.exception.ServiceException;
import com.soprasteria.modelling.framework.util.DateTimeTool;
import com.soprasteria.modelling.service.ServiceContext;

public class AccessTokenManager {
	public static int DB_TOKEN_EXPIRY = 36000;
	public static int CACHE_TOKEN_EXPIRY = 360;
	public static int CACHE_TOKEN_CAPACITY = 300;
	public static Hashtable<String, AccessToken> tokens = new Hashtable<String, AccessToken>();
	public static Queue<AccessToken> tokenqueue = new LinkedList<AccessToken>();
	
	private ServiceContext context;

	public AccessTokenManager(ServiceContext context) {
		this.context = context;
//		accesstokendao = new AccessTokenDAO(context, context.getDataTrans());
	}

	public String generateNewToken(Long userId, Long appId, String clientId, String locale, String accessdata) throws APITokenException{
		//TODO: create unique random string using String uuid = UUID.randomUUID().toString();
		// and save to database
		// return the random string back
		if(userId == null) throw new APITokenException("UserId cannot be empty", APITokenException.CODE_USER_ID);
		if(appId == null) throw new APITokenException("AppId cant be empty.", APITokenException.CODE_APP_ID);
		AccessToken usertoken = new AccessToken(userId, appId, clientId, locale, accessdata);
		try {
//			accesstokendao.create(usertoken);
		} catch (Exception e) {
			e.printStackTrace();
			throw new APITokenException("Unable to generate new token", APITokenException.CODE_TOKEN);
		}
		
		cache(usertoken);
		
		return usertoken.getToken();
	}
	
	private void cache(AccessToken token) {
		if(token == null) return;
		tokens.put(token.getToken(), token);
		tokenqueue.offer(token);
		context.log("cache token"+tokenqueue+"("+tokens.size()+")");
		
		if(tokens.size() > CACHE_TOKEN_CAPACITY) {
			AccessToken expToken = tokenqueue.poll();
			tokens.remove(expToken.getToken());
			context.log("cache exceed, clear: "+tokenqueue+"("+tokens.size()+")");
		}
	}
	
	private AccessToken fetch(String tokenString) {
		AccessToken tokenvalue = tokens.get(tokenString);
		if(tokenvalue != null) {
			if(!DateTimeTool.isExpiredInSeconds(tokenvalue.getLastMod().getTime(), CACHE_TOKEN_EXPIRY)) {
				context.log("cache token valid");
				return tokenvalue;
			} else {
				tokens.remove(tokenvalue.getToken());
				tokenqueue.remove(tokenvalue);
				context.log("cache token invalid, clear: "+tokenqueue+"("+tokens.size()+")");
			}
		}
		return null;
	}
	
	public AccessToken validateToken(String token) throws Exception {
		AccessToken tokenvalue = getToken(token);
		checkToken(tokenvalue);
//		tokenvalue.setLastMod(new Date());
//		accesstokendao.update(tokenvalue);
		context.log("token valid");
		return tokenvalue;
	}
	
	public AccessToken getToken(String token) throws Exception {
		AccessToken tokenvalue = fetch(token);
		if(tokenvalue != null) return tokenvalue;
		
//		tokenvalue = accesstokendao.getByToken(token);
		cache(tokenvalue);
		return tokenvalue;
	}
	
	private void checkToken(AccessToken token) throws Exception {
		if(token==null) {
			throw new ServiceException("Invalid token");
		}
		
		if(DateTimeTool.isExpiredInSeconds(token.getLastMod().getTime(), DB_TOKEN_EXPIRY)){
			throw new ServiceException("Token expired");
		}
	}
}
