package com.soprasteria.modelling.service;

import java.util.HashMap;

import com.soprasteria.modelling.framework.config.PropertyFileSetting;
import com.soprasteria.modelling.framework.config.SystemConfig;
import com.soprasteria.modelling.framework.context.ApplicationContext;
import com.soprasteria.modelling.framework.context.MongoTransaction;
import com.soprasteria.modelling.service.base.entity.UserDomain;

public class ServiceContext extends ApplicationContext {
	public final static String MONGODB_CONFIG_PREFIX = "mongodb";
	public final static String MONGODB_CONFIG_DB_BASE = "base";
	
	private HashMap<String, MongoTransaction> mongos;
	
	private UserDomain user;
	
	public ServiceContext() {
		super();
		loadAllTransaction();
	}
	
	public void setUser(UserDomain user) {
		this.user = user;
	}
	
	public UserDomain getUser() {
		return user;
	}
	
	private void loadAllTransaction() {
		mongos = new HashMap<>();
		HashMap<String, String> mongosetting = PropertyFileSetting.getListSetting(MONGODB_CONFIG_PREFIX, null);
		String[] mongoarray = (String[]) mongosetting.keySet().toArray(new String[0]);
		for (String mongosett : mongoarray) {
			try {
				MongoTransaction trans = MongoTransaction.getInstance(mongosetting.get(mongosett));
				mongos.put(mongosett, trans);
			} catch (Exception e) {
				log("failed to set mongo setting for "+mongosett+" due to "+e.getMessage());
			}
		}
	}
	
	public void closeAllOpenTransaction() {
		MongoTransaction[] mongoarray = (MongoTransaction[]) mongos.keySet().toArray(new MongoTransaction[0]);
		for (MongoTransaction mongotrans : mongoarray) {
			try {
				mongotrans.closeTrans();
			} catch (Exception e) {
				log("failed to close mongo trans for "+mongotrans+" due to "+e.getMessage());
			}
		}
	}
	
	public MongoTransaction getMongo(String db) {
		return MongoTransaction.getInstance(SystemConfig.config().DBHost_BASE, db);
	}

	public MongoTransaction getBaseMongo() {
		return getMongo(MONGODB_CONFIG_DB_BASE);
	}
	
	protected void finalize() {
		closeAllOpenTransaction();
	}
}
