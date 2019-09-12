package com.soprasteria.modelling.framework.util;

import java.util.ArrayList;
import java.util.Collections;

public class SortedArrayList<T> extends ArrayList<T> {
	private static final long serialVersionUID = -2697801746116644598L;
	
	private boolean isEqual4Up = true;
	
	public SortedArrayList(boolean isEqual4Up) {
		this.isEqual4Up = isEqual4Up;
	}

	@SuppressWarnings("unchecked")
    public void insertSorted(T value) {
        add(value);
        Comparable<T> cmp = (Comparable<T>) value;
        if(isEqual4Up)
        	for (int i = size()-1; i > 0 && cmp.compareTo(get(i-1)) < 0; i--)
        		Collections.swap(this, i, i-1);
        else
        	for (int i = size()-1; i > 0 && cmp.compareTo(get(i-1)) <= 0; i--)
        		Collections.swap(this, i, i-1);
    }
	
	public T min() {
		return get(0);
	}
	
	public T max() {
		return get(size()-1);
	}
}
