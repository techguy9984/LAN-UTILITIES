package com.cpjd.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerV2 implements Runnable {

	private Thread thread;
	public static volatile boolean running;
	private int port;
	
	public ServerV2(int port) {
		this.port = port;
		System.out.println("Starting server on port "+port+".");
		
		running = true;
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		Socket connection;
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port, 0, InetAddress.getLocalHost());
		} catch(Exception e) {
			System.err.println("Couldn't start server. Is an instance of the server already running?");
		}
		while(running) {
			try {
				connection = serverSocket.accept();
				new ConnectionThread(connection);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		try {
			serverSocket.close();
			thread.join();
		} catch(Exception e) {
			System.err.println("Couldn't stop server thread.");
		}
	}
	
	public static void main(String[] args) {
		int port = 52000;
		
		if(args != null && args.length > 0) port = Integer.parseInt(args[0]);
		new ServerV2(port);
	}
	
}
