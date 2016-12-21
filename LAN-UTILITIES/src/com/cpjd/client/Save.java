package com.cpjd.client;

import java.io.Serializable;

public class Save implements Serializable {
	
	private static final long serialVersionUID = 1190116670665330537L;
	
	private String name;
	private String serverIP;
	private String port;
	
	public Save(String name, String serverIP, String port) {
		this.name = name;
		this.serverIP = serverIP;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	
	public String getServerIP() {
		return serverIP;
	}
	
	public String getPort() {
		return port;
	}
}
