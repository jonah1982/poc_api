package com.soprasteria.modelling.service.site.entity;

import com.soprasteria.modelling.framework.annotation.Entity;
import com.mongodb.DBObject;

@Entity(name = "site")
public class WeatherSiteDomain extends GenericSiteDomain {
	public static String DataPoint_TEMPERATURE = "temperature";
	public static String DataPoint_RAIN = "rain";
	public static String DataPoint_HUMIDITY = "humidity";
	
	public static String SITE_TYPE = "weather";
	public static String SITE_ICON = "siteicons/weathersite.png";
	
	public WeatherSiteDomain(String siteId, String city) throws Exception {
		super(siteId, city);
		type = SITE_TYPE;
		icon = SITE_ICON;
	}

	public WeatherSiteDomain(DBObject doc) throws Exception {
		super(doc);
	}
}
