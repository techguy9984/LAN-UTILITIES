package com.cpjd.client;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientV2 implements ActionListener, Runnable {
	
	private JLabel team, ip;
	JButton copy;
	
	public ClientV2() {
		JFrame frame = new JFrame("Cats PJs LAN Client Version 1");
		frame.setSize(400, 200);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		copy = new JButton("COPY IP");
		ip = new JLabel("Trying to connect to server... ");
		team = new JLabel("Team: ");
		JButton settings = new JButton("Settings");
		
		ip.setBounds(5,10,395,25);
		copy.setBounds(5,35,100,25);
		team.setBounds(5, 58, 395, 25);
		settings.setBounds(5, 140, 100, 25);
		
		copy.setBorderPainted(false);
		copy.setBackground(Color.MAGENTA);
		copy.setForeground(Color.BLACK);
		copy.setFocusable(false);
		copy.addActionListener(this);
		
		settings.setBorderPainted(false);
		settings.setBackground(Color.DARK_GRAY);
		settings.setForeground(Color.WHITE);
		settings.setFocusable(false);
		settings.addActionListener(this);
		
		frame.add(copy);
		frame.add(ip);
		frame.add(team);
		frame.add(settings);
		frame.setVisible(true);
		
		new Settings(true);
		
		new Thread(this).start();
	}
	
	public void run() {
		while(true) {
			try {
				String response = update();
				ip.setText("The gaming server's IP is: "+response.split(",")[0]);
				if(response.split(",")[1].equals("1")) team.setText("Team: Terrorist");
				else team.setText("Team: Counter-terrorist");
				
				Thread.sleep(5000);
			} catch(Exception e) {
				System.err.println("Failed to update. Trying again in 5 seconds.");
			}
		}
	}
	
	public String update() throws Exception {
		Save slot = Settings.getSave();
		Socket clientSocket = new Socket(slot.getServerIP(), Integer.parseInt(slot.getPort()));
		DataOutputStream stream = new DataOutputStream(clientSocket.getOutputStream());
		InputStream in = clientSocket.getInputStream();
		stream.writeBytes("get,"+slot.getName()+"|");
		String response = "";
        while (true) {
            int ch = in.read();
            if ((ch < 0) || (ch == '|')) {
                break;
            }
           response += (char)ch;
        }
		clientSocket.close();
		return response;
	}
	
	public static void main(String[] args) {
		new ClientV2();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == copy) {
			StringSelection stringSelection = new StringSelection("connect "+ip.getText().split(":")[1].trim());
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
		} else {
			new Settings(false);
		}
	}
	
}
