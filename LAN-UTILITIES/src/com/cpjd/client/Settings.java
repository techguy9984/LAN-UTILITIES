package com.cpjd.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Settings implements ActionListener {

	private static File dir;
	
	private JTextField name, server;
	private JFrame frame;
	
	public Settings(boolean setup) {
		String osName = System.getProperty("os.name").toLowerCase();
		
		if(osName.contains("win")) {
			dir = new File((System.getenv("APPDATA") + File.separator + "LAN-UTILS" + File.separator));
		} else if(osName.contains("mac")) {
			dir = new File(System.getProperty("user.home") + "/Library/Application Support/LAN-UTILS"+File.separator);
		} else if(osName.contains("nux")) {
			dir = new File(System.getProperty("user.home"));
		}
		
		if(!dir.exists()) dir.mkdir();
		
		Save saveSlot = getSave();
		
		if(saveSlot == null) { 
			save(new Save("", "192.168.1.26", "52000"));
			saveSlot = getSave();
		}
		
		frame = new JFrame("Settings");
		frame.setSize(200, 200);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel nameLabel = new JLabel("Your first name:");
		name = new JTextField(saveSlot.getName());
		JLabel serverLabel = new JLabel("Server IP,Port");
		server = new JTextField(saveSlot.getServerIP()+","+saveSlot.getPort());
		JButton save = new JButton("Save");
		
		nameLabel.setBounds(5, 10, 150, 25);
		name.setBounds(5, 35, 150, 25);
		serverLabel.setBounds(5, 60, 100, 25);
		server.setBounds(5, 85, 150, 25);
		save.setBounds(5,120, 100, 25);
		
		save.setBorderPainted(false);
		save.setBackground(Color.GREEN);
		save.setForeground(Color.BLACK);
		save.setFocusable(false);
		save.addActionListener(this);
		
		frame.add(nameLabel);
		frame.add(name);
		frame.add(serverLabel);
		frame.add(server);
		frame.add(save);
		
		if(setup) frame.dispose();
		else frame.setVisible(true);
	}
	
	public void save(Save save) {
		serializeObject(save, "save.ser");
	}
	
	public static Save getSave() {
		return (Save) deserializeObject("save.ser");
	}
	
	private static boolean serializeObject(Object object, String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(dir + File.separator + fileName);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(object);
			out.close();
			fos.close();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("Couldn't save game.");
			return false;
		}
	}
	
	private static Object deserializeObject(String location) {
		try {
			FileInputStream fis = new FileInputStream(dir + File.separator + location);
			ObjectInputStream in = new ObjectInputStream(fis);
			Object o = in.readObject();
			in.close();
			fis.close();
			return o;
		} catch(Exception e) {
			System.err.println("Couldn't find or read game save. Creating new game save.");
			return null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			save(new Save(name.getText(), server.getText().split(",")[0], server.getText().split(",")[1]));
			frame.dispose();
		} catch(Exception ex) {
			server.setText("Server IP was NOT configured correctly.");
		}
		
	}
	
}
