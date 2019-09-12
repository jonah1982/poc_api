package com.soprasteria.modelling.framework.entity.domain;

import java.util.Hashtable;
import java.util.Set;

public abstract class SingleKeySQLDomain extends SQLDomain {

	public SingleKeySQLDomain() {
		super();
	}

	public SingleKeySQLDomain(Hashtable<String, Object> table) {
		super(table);
	}

	public String toSQLUpdate() {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE "+getTableName()+" ");
		sb.append("SET ");
		Set<String> columns = table.keySet();
		for (String string : columns) {
			sb.append(string+"="+getSQLValue(string)+", ");
		}
		sb.replace(sb.length()-2, sb.length()-1, " WHERE ");
		if(getPrimaryKey() != null) {
			sb.append(getPrimaryKey()+"='"+table.get(getPrimaryKey())+"';");
		}
		return sb.toString();
	}

	public String toSQLDelete() {
		String sql = "DELETE FROM "+getTableName()+" WHERE "+getPrimaryKey()+"="+getOid()+";";
		return sql;
	}

	public abstract String getPrimaryKey();

	public void setOid(Long oid) {
		set(getPrimaryKey(), oid);
	}

	public Long getOid() {
		return getLongField(getPrimaryKey());
	}

}
