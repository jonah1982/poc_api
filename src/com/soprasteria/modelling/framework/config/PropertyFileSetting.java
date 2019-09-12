package com.soprasteria.modelling.framework.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import com.soprasteria.modelling.framework.context.ApplicationContext;
import com.soprasteria.modelling.framework.util.Tool;

public class PropertyFileSetting {

	private static List<ResourceBundle> loadSetting(String contextPostFixId) {
		List<ResourceBundle> settings = new ArrayList<>();
		try {
			settings.add(ResourceBundle.getBundle("setting_"+contextPostFixId,Locale.getDefault()));
		} catch (Exception e) {
		}
		settings.add(ResourceBundle.getBundle("setting",Locale.getDefault()));
		return settings;
	}

	public static String getSetting(String key, String contextPostFixId) {
		List<ResourceBundle> settings = loadSetting(contextPostFixId);

		for (ResourceBundle resourceBundle : settings) {
			String value = null;
			try {
				value = resourceBundle.getString(key);
			} catch (Exception e) {
			}
			if(value != null) return value.trim();
		}

		return null;
	}

	public static int getIntSetting(String key, String contextPostFixId){
		String value = null;
		int intValue = 0;
		try {
			value = getSetting(key, contextPostFixId);
			intValue = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			System.out.println("setting error: setting for "+key+" is not integer: "+value);
		}
		return intValue;
	}

	public static boolean getBooleanSetting(String key, String contextPostFixId){
		String value = null;
		boolean bValue = false;
		try{
			value = getSetting(key, contextPostFixId);
			if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("Y"))
				bValue = true;
		} catch(Exception e){
			System.out.println("setting error: setting for " + key + " is not boolean type: "+value);
		}
		return bValue;
	}

	public static HashMap<String, String> getListSetting(String keyPatern, String contextPostFixId) {
		HashMap<String, String> values = new HashMap<>();
		List<ResourceBundle> settings = loadSetting(contextPostFixId);

		for (ResourceBundle resourceBundle : settings) {
			Set<String> keys = resourceBundle.keySet();
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				if(key.startsWith(keyPatern)) values.put(key, resourceBundle.getString(key));
			}
		}

		return values;
	}

	public static List<Class<?>> getPlugins(String classpath, String contextPostFixId) {
		List<Class<?>> classlist = new ArrayList<>();
		String classesstring = getSetting("plugin."+classpath, contextPostFixId);
		if(classesstring != null) {
			StringTokenizer st = new StringTokenizer(classesstring, ";");
			while(st.hasMoreTokens()) {
				try {
					String classstr = st.nextToken();
					if(!Tool.isEmpty(classstr)) {
						classstr = classstr.trim();
						if(!classstr.contains(".")) classstr = classpath.substring(0, classpath.lastIndexOf("."))+"."+classstr;
						Class<?> clazz = Class.forName(classstr);
						classlist.add(clazz);
					}
				} catch (ClassNotFoundException e) {}
			}
		}
		return classlist;
	}
	
	public static List<Class<?>> getPluginsFromProperty(String pluginBase, String key) {
		try {
			List<Class<?>> classlist = new ArrayList<>();
			System.out.println("trying to load system property: "+key);
			String classesstring = System.getProperty(key);
			System.out.println("get system property: "+classesstring);
			StringTokenizer st = new StringTokenizer(classesstring, ";");
			while(st.hasMoreTokens()) {
				String classstr = st.nextToken();
				if(!Tool.isEmpty(classstr)) {
					if(!classstr.contains(".")) classstr = pluginBase+"."+classstr;
					Class<?> clazz = Class.forName(classstr);
					classlist.add(clazz);
				}
			}
			return classlist;
		} catch (Exception e) {
			new ApplicationContext().log("Plugin setting wrong with key: "+key+" due to "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<String, String> getAllSetting(String contextPostFixId) {
		HashMap<String, String> values = new HashMap<>();
		List<ResourceBundle> settings = loadSetting(contextPostFixId);

		for (ResourceBundle resourceBundle : settings) {
			Set<String> keys = resourceBundle.keySet();
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				values.put(key, resourceBundle.getString(key));
			}
		}

		return values;
	}
}
