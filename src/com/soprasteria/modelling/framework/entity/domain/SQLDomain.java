package com.soprasteria.modelling.framework.entity.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Set;

import com.google.gson.Gson;
import com.soprasteria.modelling.framework.util.DateTimeTool;

public abstract class SQLDomain {
	protected Hashtable<String, Object> table;

	public abstract String getTableName();
	
	public SQLDomain() {
		table = new Hashtable<String, Object>();
	}
	
	public SQLDomain(Hashtable<String, Object> table) {
		this.table = table;
	}
	
	public void set(String column, Object value) {
		if(value != null)
			table.put(column, value);
	}
	
	public boolean isDateField(String field) {
		Object o = getField(field);
		if(o instanceof java.sql.Date) return true;
		if(o instanceof java.util.Date) return true;
		
		return false;
	}
	
	public Object getField(String field) {
		return table.get(field);
	}
	
	public String getSQLValue(String field) {
		Object o = table.get(field);
		
		if(o == null) return ""+o;
		
		if(o instanceof Integer || o instanceof Long || o instanceof Double) {
			return o.toString();
		}
		
		String s = o.toString();
		
		s = s.replaceAll("\'", "");
		s = s.replaceAll("\\\\", "");
		
		if(o instanceof java.util.Date) {
			s = DateTimeTool.convertUtilDatetoSQLDate((java.util.Date)o).toString();
		}
		
		return "'"+s+"'";
	}
	
	public String getStringField(String field) {
		Object o = table.get(field);
		
		if (o != null){
			return o.toString();
		}
		return "";
	}
	
	public BigDecimal getDoubleField(String field) {
		Object o = table.get(field);
		
		try {
			if(o != null) {
				if (o instanceof Double) return new BigDecimal((Double)o);
				else return new BigDecimal(o.toString());
			}
		} catch (NumberFormatException e) {
		}
		
		return null;
	}
	
	public Long getLongField(String field) {
		Object o = table.get(field);
		
		try {
			if(o != null) {
				if (o instanceof Long) return (Long)o;
				else return Long.parseLong(o.toString());
			}
		} catch (NumberFormatException e) {
		}
		
		return null;
	}
	
	public Integer getIntegerField(String field) {
		Object o = table.get(field);
		
		try {
			if(o != null) {
				if (o instanceof Integer) return (Integer)o;
				else return Integer.parseInt(o.toString());
			}
		} catch (NumberFormatException e) {
		}
		
		return null;
	}
	
	public Boolean getBooleanField(String field){
		Object o = table.get(field);
		try{
			if(o != null)
				if (o instanceof Boolean) return (Boolean)o;
				else return Boolean.parseBoolean(o.toString());
		}	catch (NumberFormatException e) {
		}
		return false;
	}
	
	public java.util.Date getDateField(String field) {
		Object o = table.get(field);
		
		if(o instanceof java.sql.Date) {
			return DateTimeTool.convertSQLDatetoUtiLDate((java.sql.Date)o);
		} else 
			return (java.util.Date)o;
	}
	
	public void setDateField(String field, java.util.Date date) {
		if(date == null) return;
		set(field, DateTimeTool.convertUtilDatetoSQLDate(date));
	}
	
	public Timestamp getTimestampField(String field) {
		Object o = table.get(field);
		
		if(o instanceof Timestamp) {
			return (Timestamp) o;
		} else 
			return Timestamp.valueOf(o.toString());
	}
	
	public String toSQLInsertion() {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into "+getTableName()+" ");
		Set<String> columns = table.keySet();
		sb.append(columns.toString().replaceFirst("\\[", "(").replaceFirst("\\]", ")")+" ");
		sb.append("values (");
		for (String string : columns) {
			sb.append(""+getSQLValue(string)+", ");
		}
		sb.replace(sb.length()-2, sb.length()-1, ");");
		
		return sb.toString();
	}
	
	public String toSQLReplace() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("replace into "+getTableName()+" ");
		Set<String> columns = table.keySet();
		sb.append(columns.toString().replaceFirst("\\[", "(").replaceFirst("\\]", ")")+" ");
		sb.append("values (");
		for (String string : columns) {
			sb.append(""+getSQLValue(string)+", ");
		}
		sb.replace(sb.length()-2, sb.length()-1, ");");
		
		return sb.toString();
	}
	
	public String toString() {
		return getTableName()+"=>"+table;
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}
	
	public Hashtable<String, Object> toTable() {
		return table;
	}
	
}
