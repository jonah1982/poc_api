/**
 * @author Jonah Wang
 * Mar 23, 2011
 */
package com.soprasteria.modelling.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeTool {
	public static final long SECOND = 1000;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;
	public static final long WEEK = DAY * 7;

	public static final String ISO8601_FORMATTER_STRING = "yyyy-MM-dd'T'HH:mm'Z'";

	public static Date get1WeekAfter(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		return calendar.getTime();
	}

	public static Date getHourAfter(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	public static Date getDaysAfter(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}

	public static Date getNextWeek() {
		Calendar calendar = Calendar.getInstance();
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		int days = (Calendar.SATURDAY - weekday) % 7;
		calendar.add(Calendar.DAY_OF_YEAR, days + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getMonthStart(int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getMonthStart(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// int month = cal.get(Calendar.MONTH);
		//
		// Calendar c = Calendar.getInstance();
		// c.set(Calendar.MONTH, month);
		// c.set(Calendar.DAY_OF_MONTH, 1);
		// c.set(Calendar.HOUR_OF_DAY, 0);
		// c.set(Calendar.MINUTE, 0);
		// c.set(Calendar.SECOND, 0);
		// c.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getEndOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getEnd(date));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static int getDaysoOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static String getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);

		String monthString;
		switch (month + 1) {
		case 1:
			monthString = "January";
			break;
		case 2:
			monthString = "February";
			break;
		case 3:
			monthString = "March";
			break;
		case 4:
			monthString = "April";
			break;
		case 5:
			monthString = "May";
			break;
		case 6:
			monthString = "June";
			break;
		case 7:
			monthString = "July";
			break;
		case 8:
			monthString = "August";
			break;
		case 9:
			monthString = "September";
			break;
		case 10:
			monthString = "October";
			break;
		case 11:
			monthString = "November";
			break;
		case 12:
			monthString = "December";
			break;
		default:
			monthString = "Invalid month";
			break;
		}

		return monthString;
	}

	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	public static int getHour(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR);
	}
	public static int getDateField(Date date, int field) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(field);
	}

	public static int getDayofYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	public static Date makeDate(TimeZone tz, Date date, int hr, int min, int sec, int millisec) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance(tz);
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hr);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);
		c.set(Calendar.MILLISECOND, millisec);
		return c.getTime();
	}

	public static Date getStart(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getEnd(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	public static Date convertToTimeStamp(String yyyymmdd) {
		SimpleDateFormat sdf = null;
		if (yyyymmdd.indexOf('-') != -1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			sdf = new SimpleDateFormat("yyyyMMdd");
		}
		sdf.setTimeZone(TimeZone.getDefault());
		Date d = sdf.parse(yyyymmdd, new ParsePosition(0));
		return d;
	}

	public static Date convertToDateWithYYYYDDDHHMMSS(String strDate) throws ParseException {
		return parseDate(TimeZone.getDefault(), "yyyyDDDHHmmss", strDate);
	}

	public static String getDateString(String format, Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		return getDateString(tz, format, date);
	}

	public static String getDateString(TimeZone tz, String format, Date date) {
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(tz);
		return df.format(date);
	}

	public static Date parseDate(TimeZone tz, String format, String datestr) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(tz);
		return df.parse(datestr);
	}

	public static long getCurrentTime() {
		java.util.Date today = new java.util.Date();
		return today.getTime();
	}

	public static java.sql.Timestamp convertUtilDatetoSQLDate(java.util.Date date) {
		return new java.sql.Timestamp(date.getTime());

	}

	public static java.util.Date convertSQLDatetoUtiLDate(java.sql.Date date) {
		return new java.util.Date(date.getTime());

	}

	public static String ISO8601format(Date date) {
		return getDateString(ISO8601_FORMATTER_STRING, date);
	}

	public static boolean isExpiredInSeconds(long timestamp, int seconds) {
		timestamp = convertToNormalTimestamp(timestamp);
		System.out.println("request time: " + timestamp);
		long servertime = new Date().getTime();
		System.out.println("server time: " + servertime);
		System.out.println("time diff: " + (servertime - timestamp) / 1000);
		long elapsedtime = Math.abs(servertime - timestamp);
		if (elapsedtime < seconds * 1000) {
			System.out.println("valid");
			return false;
		}

		return true;
	}

	public static long convertToNormalTimestamp(long timestamp) {
		// while((""+timestamp).length()>10) timestamp = timestamp / 10;
		return timestamp;
	}

	public static Long convertResolutionTime(String resolution) {
		if (resolution == null)
			return null;
		if (resolution.equals("1/8s"))
			return (long) 1000 / 8;
		if (resolution.equals("1/4s"))
			return (long) 1000 / 4;
		if (resolution.equals("1/2s"))
			return (long) 1000 / 2;
		if (resolution.equals("0.5n"))
			return (long) 30 * 1000;
		if (resolution.equals("1n"))
			return (long) 60 * 1000;
		if (resolution.equals("5n"))
			return (long) 5 * 60 * 1000;
		if (resolution.equals("15n"))
			return (long) 15 * 60 * 1000;
		if (resolution.equals("30n"))
			return (long) 30 * 60 * 1000;
		if (resolution.equals("1h"))
			return (long) 60 * 60 * 1000;
		if (resolution.equals("6h"))
			return (long) 6 * 60 * 60 * 1000;
		if (resolution.equals("12h"))
			return (long) 12 * 60 * 60 * 1000;
		if (resolution.equals("1d"))
			return (long) 24 * 60 * 60 * 1000;
		if (resolution.equals("1w"))
			return (long) 7 * 24 * 60 * 60 * 1000;
		if (resolution.equals("1m"))
			return (long) 30 * 24 * 60 * 60 * 1000;
		if (resolution.equals("6m"))
			return (long) 6 * 30 * 24 * 60 * 60 * 1000;
		if (resolution.equals("1y"))
			return (long) 12 * 30 * 24 * 60 * 60 * 1000;

		return null;
	}

	public static Date getConvergedTime(Date time, long convergeRate) throws Exception {
		if (time == null)
			throw new Exception("the time to be converted cant be null.");
		Long timestamp = time.getTime();
		return new Date(timestamp - timestamp % convergeRate);
	}

	public static boolean validTimeZone(String id) {
		for (String tzId : TimeZone.getAvailableIDs()) {
			if (tzId.equals(id))
				return true;
		}
		return false;
	}
}
