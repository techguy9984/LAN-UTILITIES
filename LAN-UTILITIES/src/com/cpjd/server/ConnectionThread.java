package com.cpjd.server;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The ConnectionThread handles all clients and updates them with the required information.
 * The ConnectionThread accepts the following server commands:
 * 
 * get,[name]       									Returns the respective team of the player along with the server's IP.
 * push,#entries,[name-team],[name-team],etc			Updates the server's active team queue. As soon as the data is set, clients will start updating.
 * quit2609											    Closes the server.
 * 
 * Every request sent to the server must end with a '|'.
 * @author Will Davies
 *
 */
public class ConnectionThread implements Runnable {
	
	private static ArrayList<String> TEAMS;
	private static String IP;
	
	private Socket socket;
	private Thread thread;
	
	public ConnectionThread(Socket socket) {
		this.socket = socket;
		
		TEAMS = new ArrayList<String>();
		TEAMS.add("jake-t");
		TEAMS.add("will-ct");
		IP = "cpjd.zapto.com";
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			String request = "";
	        while (true) {
	            int ch = in.read();
	            if ((ch < 0) || (ch == '|')) {
	                break;
	            }
	           request += (char)ch;
	        }
	        String response = getResponse(request);
	        System.out.println("Received request from client: "+request+" Replying: "+response);
	        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	        out.writeBytes(response);
	        Thread.sleep(3000);
	        socket.close();
	        thread.join();
		} catch(Exception e) { 
			System.err.print("Failed to update a client.");
			e.printStackTrace();
		}
	}
	
	private String getResponse(String request) {
		if(request.split(",")[0].contains("get")) return getTeam(request.split(",")[1]);
		if(request.split(",")[0].contains("push")) return createDatabase(request);
		if(request.contains("quit2609")) Server.running = false;
		return "Server did not recognize your request.";
	}
	
	/**
	 * Pushes the recieved update data to the server. Clients will received this information.
	 * @param line The entire line push request line.
	 * @return Result of push.
	 */
	private String createDatabase(String line) {
		TEAMS = new ArrayList<String>();
		
		for(int i = 2; i < Integer.parseInt(line.split(",")[1] + 1); i++) {
			TEAMS.add(line.split(",")[i]);
		}
		
		IP = line.split(",")[1];
		
		return "Data was updated successfully.";
	}
	
	/**
	 * Returns the team of the player conjugated with the server IP
	 * @param name The name of the player, must match database stored name
	 * @return The team the player is on along with IP.
	 */
	private String getTeam(String name) {
		if(TEAMS == null) return "Teams info isn't available yet.";
		
		for(int i = 0; i < TEAMS.size(); i++) {
			if(TEAMS.get(i).split("-")[0].equalsIgnoreCase(name.trim())) {
				return IP + ","+TEAMS.get(i).split("-")[1];
			}
		}
		return "Teams info isn't available yet.";
	}

}
