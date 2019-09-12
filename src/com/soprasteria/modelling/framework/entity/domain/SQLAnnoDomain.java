package com.soprasteria.modelling.framework.entity.domain;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.util.DateTimeTool;
import com.soprasteria.modelling.framework.util.Tool;

public abstract class SQLAnnoDomain extends SQLDomain {
	public SQLAnnoDomain() {}
	public SQLAnnoDomain(ResultSet rs) throws Exception {
		if(getClass().isAnnotationPresent(Entity.class)) {
			List<Field> fields = Tool.getFields(getClass());
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					field.setAccessible(true);
					Object value = rs.getObject(field.getName());
					if(value != null) {
						try {
							field.set(this, value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {
			throw new Exception("This domain "+getClass()+" is not defined as Entity");
		}
	}
	
	public String getTableName() {
		if(getClass().isAnnotationPresent(Entity.class)) {
			Entity entity = getClass().getAnnotation(Entity.class); 
			String tableName = entity.name();
			if(Tool.isEmpty(tableName)) return "";
			
			return tableName;
		} 
		return "";
	}
	
	public HashMap<String, Object> toMap() throws Exception {
		HashMap<String, Object> doc = new HashMap<>();
		
		if(getClass().isAnnotationPresent(Entity.class)) {
			List<Field> fields = Tool.getFields(getClass());
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					field.setAccessible(true);
					Column column = field.getAnnotation(Column.class);
					Object value = field.get(this);
					if(!column.isNull() && value == null) throw new Exception ("Non-nullable field ("+field.getName()+") indicated");
					doc.put(field.getName().toLowerCase(), value);
				}
			}
		} else {
			throw new Exception("This domain "+getClass()+" is not defined as Entity");
		}
		return doc;
	}
	
	public String toSQLReplace() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("replace into "+getTableName()+" ");
		
		HashMap<String, Object> map = toMap();
		Set<String> columns = map.keySet();
		sb.append(columns.toString().replaceFirst("\\[", "(").replaceFirst("\\]", ")")+" ");
		sb.append("values (");
		for (String string : columns) {
			sb.append(""+toSQLValue(map.get(string))+", ");
		}
		sb.replace(sb.length()-2, sb.length()-1, ");");
		
		return sb.toString();
	}
	
	public String toSQLValue(Object o) {
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
	
	public String toString() {
		String tableName = getTableName();
		String tableContent = "";
		try {tableContent = toMap().toString();} catch (Exception e) {}
		
		return tableName+"=>"+tableContent;
	}
}
