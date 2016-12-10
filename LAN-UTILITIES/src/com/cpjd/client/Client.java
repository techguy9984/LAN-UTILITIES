package com.cpjd.client;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Client implements Runnable {
	
	private Thread thread;
	
	private JFrame frame;
	private JLabel label;
	private String name;
	private JTextField input;
	
	private final int UPDATE_INTERVAL = 3; // how often to check for an update
	
	private String TARGET_IP = "cpjd.zapto.org";
	
	public Client() {
		frame = new JFrame("LAN Party Client");
		frame.setSize(300, 200);
		frame.setLocation(new Point(0,0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		label = new JLabel("Server unavailable.");
		label.setLocation(5, 15);
		label.setSize(200, 25);
		frame.add(label);
		input = new JTextField();
		input.setText("Enter your first name");
		input.addMouseListener(new MouseAdapter() {
			  @Override
			  public void mouseClicked(MouseEvent e) {
			    input.setText("");
			  }
			});
		input.setLocation(5,50);
		input.setSize(200, 25);
		frame.add(input);
		frame.setVisible(true);
		
		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
	
	public void run() {
		try {
			Thread.sleep(UPDATE_INTERVAL * 1000);
			label.setText(update(input.getText()));
		} catch(Exception e) {}
	}
	
	public String update(String name) {
		// gets IP:Team message
		return "Connect to IP: ";
	}
	
	public static void main(String[] args) {
		new Client();
	}
	
}
