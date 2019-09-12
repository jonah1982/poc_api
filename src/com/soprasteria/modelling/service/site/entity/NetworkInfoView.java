package com.soprasteria.modelling.service.site.entity;

import java.util.TreeMap;


public class NetworkInfoView {
	public String stationid;
	public String stationname;
	public boolean onportal;
	
	public TreeMap<String, StationNetworkInfo> networks = new TreeMap<>();
	
	public NetworkInfoView(String stationid, String stationname, boolean onportal){
		setStationid(stationid);
		setStationname(stationname);
		setOnportal(onportal);
	}
	
	public String getStationid() {
		return stationid;
	}


	public void setStationid(String stationid) {
		this.stationid = stationid;
	}


	public String getStationname() {
		return stationname;
	}


	public void setStationname(String stationname) {
		this.stationname = stationname;
	}
	
	
	public boolean isOnportal() {
		return onportal;
	}

	
	public void setOnportal(boolean onportal) {
		this.onportal = onportal;
	}

	
	public TreeMap<String, StationNetworkInfo> getNetworks() {
		return networks;
	}


	public void addNetwork(String networktime, StationNetworkInfo network) {
		this.networks.put(networktime, network);
	}
}

