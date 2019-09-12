package com.soprasteria.modelling.api.auth;

import java.util.Date;

public class TimestampValidation {

	public static boolean isValidInSeconds(long timestamp, int seconds) {
		System.out.println("request time: "+timestamp);
		System.out.println("server time: "+new Date().getTime());
		long elapsedtime = Math.abs(new Date().getTime() - timestamp);
		if(elapsedtime < seconds * 1000) {
			return true;
		}
		
		return false;
	}

}
