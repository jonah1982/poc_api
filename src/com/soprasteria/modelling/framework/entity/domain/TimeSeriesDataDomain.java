package com.soprasteria.modelling.framework.entity.domain;

import java.util.Date;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.soprasteria.modelling.framework.util.DateTimeTool;

public class TimeSeriesDataDomain extends MongoDomain {
	public static String COL_VALUES= "values";
	public static String COL_TS= "timestamp";
	public static String COL_CONVERGERATE = "converge_rate";
	
	public static String toOid(String datapointId, Date timestamp, long convergeRate) throws Exception {
		return datapointId.toLowerCase()+"-"+DateTimeTool.getConvergedTime(timestamp, convergeRate).getTime();
	}
	public static String genDataPointId(String oid) {
		return oid.substring(0, oid.lastIndexOf("-"));
	}
	
	public TimeSeriesDataDomain(String collectionName, String datapointId, Date timestamp, long convergeRate) throws Exception {
		super(collectionName, toOid(datapointId, timestamp, convergeRate));
		setField(COL_TS, DateTimeTool.getConvergedTime(timestamp, convergeRate));
		setField(COL_CONVERGERATE, convergeRate);
	}
	
	public TimeSeriesDataDomain(String collectionName, DBObject doc) throws Exception {
		super(collectionName, doc);
	}
	
	public Date getTimestamp() {
		return getDateField(COL_TS);
	}
	
	public String getDataPointId() {
		return genDataPointId(getOid().toString());
	}
	
	public Long getConvergeRate() {
		return getLongField(COL_CONVERGERATE);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getValuez() {
		return (HashMap<String, Object>)getField(COL_VALUES);
	}
	
	public void setValuez(HashMap<String, Object> valuez) {
		HashMap<String, Object> existingValue = getValuez();
		if (existingValue != null) {
			existingValue.putAll(valuez);
		} else {
			existingValue = valuez;
		}
		setField(COL_VALUES, existingValue);
	}
	
	public void setSingleValue(String timestamp, Object value) {
		HashMap<String, Object> existingValue = getValuez();
		if (existingValue == null) {
			existingValue = new HashMap<>();
		}
		existingValue.put(timestamp, value);
		setField(COL_VALUES, existingValue);
	}
	
	public void resetValuez(HashMap<String, Double> valuez) {
		setField(COL_VALUES, valuez);
	}
	
	public DBObject toSetStmt() {
		DBObject doc1 = new BasicDBObject();
		DBObject doc2 = new BasicDBObject();
		
		HashMap<String, Object> valuez = getValuez();

		if (valuez == null || valuez.isEmpty()) return null;

		String[] keys = (String[]) valuez.keySet().toArray(new String[0]);

		for (String key : keys) {
			doc2.put(COL_VALUES+"."+key, valuez.get(key));
		}

		doc2.putAll(toUpdateLastModStmt());

		doc1.put("$set", doc2);
		return doc1;
	}
	
	public HashMap<Long, TimeSeriesDataDomain> converge() throws Exception {
		HashMap<Long, TimeSeriesDataDomain> domainlist = new HashMap<>();
		
		HashMap<String, Object> valuez = getValuez();
		if (valuez == null || valuez.isEmpty()) return domainlist;
		
		String[] keys = (String[]) valuez.keySet().toArray(new String[0]);
		for (String key : keys) {
			Long timestamp = DateTimeTool.getConvergedTime(new Date(Long.parseLong(key)), getConvergeRate()).getTime();
			
			if(!domainlist.containsKey(timestamp)) {
				TimeSeriesDataDomain domain = new TimeSeriesDataDomain(getCollectionName(), getDataPointId(), new Date(timestamp), getConvergeRate());
				domainlist.put(timestamp, domain);
			}
			TimeSeriesDataDomain domain = domainlist.get(timestamp);
			domain.setSingleValue(key, valuez.get(key));
		}
		
		return domainlist;
	}
}
