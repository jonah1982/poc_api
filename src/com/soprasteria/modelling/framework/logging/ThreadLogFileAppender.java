package com.soprasteria.modelling.framework.logging;

import org.apache.log4j.DailyRollingFileAppender;

public class ThreadLogFileAppender extends DailyRollingFileAppender {

	@Override
	public void setFile(String file) {
		super.setFile(file+"_"+Thread.currentThread().getId()+".log");
	}
}
