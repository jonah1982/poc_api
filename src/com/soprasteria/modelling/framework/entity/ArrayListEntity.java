package com.soprasteria.modelling.framework.entity;

import java.util.ArrayList;

import com.google.gson.Gson;

public abstract class ArrayListEntity<T> {
	public abstract void setArray(ArrayList<T> array);
	public abstract ArrayList<T> getArray();

	public abstract String getArrayName();

	public ArrayListEntity() {
		setArray(new ArrayList<T>());
	}

	public ArrayListEntity(ArrayList<T> array) {
		setArray(array);
	}

	public void append(T value) {
		if(value != null)
			getArray().add(value);
	}
	
	public void set(int pos, T value) {
		if(value != null)
			getArray().add(pos, value);
	}

	public Object getField(int field) {
		return getArray().get(field);
	}

	public String getStringField(int field) {
		Object o = getArray().get(field);

		if (o != null){
			return o.toString();
		}
		return "";
	}

	public Double getDoubleField(int field) {
		Object o = getArray().get(field);

		try {
			if(o != null) {
				if (o instanceof Double) return (Double)o;
				else return Double.parseDouble(o.toString());
			}
		} catch (NumberFormatException e) {
		}

		return null;
	}

	public Long getLongField(int field) {
		Object o = getArray().get(field);

		try {
			if(o != null) {
				if (o instanceof Long) return (Long)o;
				else return Long.parseLong(o.toString());
			}
		} catch (NumberFormatException e) {
		}

		return null;
	}

	public Integer getIntegerField(int field) {
		Object o = getArray().get(field);

		try {
			if(o != null) {
				if (o instanceof Integer) return (Integer)o;
				else return Integer.parseInt(o.toString());
			}
		} catch (NumberFormatException e) {
		}

		return null;
	}

	public Boolean getBooleanField(int field){
		Object o = getArray().get(field);
		try{
			if(o != null)
				if (o instanceof Boolean) return (Boolean)o;
				else return Boolean.parseBoolean(o.toString());
		}	catch (NumberFormatException e) {
		}
		return false;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}
}
