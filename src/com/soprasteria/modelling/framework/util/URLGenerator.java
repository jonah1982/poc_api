package com.soprasteria.modelling.framework.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class URLGenerator {

	private static String DEFAULT_ENCODING_FOR_URL = "UTF-8";

	private String address;
	private Map<String, String> map = new HashMap<String, String>();

	public URLGenerator() {
		this.address = "";
	}

	public URLGenerator(String address) {
		this.address = address;
	}
	
	public URLGenerator(URL address) {
		this.address = address.toString();
	}


	public String getParameter(String param) {
		return map.get(param);
	}

	public void setParameter(String param, Object value) {
		map.put(param, value.toString());
	}

	public void unsetParameter(String param) {
		map.remove(param);
	}

	public URL getUrl() throws MalformedURLException {
		StringBuilder sb = new StringBuilder(address);

		List<String> listOfParams = new ArrayList<String>();
		for (String param : map.keySet()) {
			listOfParams.add(param + "=" + encodeString(map.get(param)));
		}

		if (!listOfParams.isEmpty()) {
			String query = StringUtils.join(listOfParams, "&");
			sb.append("?");
			sb.append(query);
		}

		return new URL(sb.toString());
	}

	public static String encodeString(String name) throws NullPointerException {
		String tmp = null;

		if (name == null)
			return null;

		try {
			tmp = java.net.URLEncoder.encode(name, DEFAULT_ENCODING_FOR_URL);
		} catch (Exception e) {}

		if (tmp == null)
			throw new NullPointerException();

		return tmp;
	}

}
