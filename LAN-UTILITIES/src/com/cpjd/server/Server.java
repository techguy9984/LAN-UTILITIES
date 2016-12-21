package com.cpjd.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

	private Thread thread;
	public static volatile boolean running;
	private int port;
	
	public Server(int port) {
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
			System.err.println("Couldn't start server");
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
		new Server(port);
	}
	
}
