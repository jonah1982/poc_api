package com.soprasteria.modelling.framework.config;

public class SystemConfig {
	private static SystemConfig config;
	public static SystemConfig config() {
		if(config == null) config = new SystemConfig();
		return config;
	}
	
	private SystemConfig() {}
	
	public String DBHost_BASE = "dws0";
}
