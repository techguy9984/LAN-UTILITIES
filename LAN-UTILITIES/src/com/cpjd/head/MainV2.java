package com.cpjd.head;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.cpjd.head.MatchFinder.Match;
import com.cpjd.head.Prefs.Save;

public class MainV2 {
	
	private ArrayList<String> queue = new ArrayList<String>();
	private ArrayList<String> database;
	
	private boolean running;
	
	private String serverIP = "192.168.0.2";
	private int serverPort = 52000;
	
	private ArrayList<Match> matches;
	private Match onDeck;
	
	private String team1 = "Counter-Terrorist";
	private String team2 = "Terrorist";
	
	public MainV2() {
		running = true;

		Scanner scanner = new Scanner(System.in);
		Prefs.initDirs();
		System.out.println("Welcome to the lan utilities command line. Type <help> for help.");

		while (running) {
			String input = scanner.nextLine();
			Save save = Prefs.deserializeSave();
			if(save != null) database = save.getDatabase();
			try {
				if (input.equals("help")) {
					System.out.println("Command						Description");
					System.out.println("add <name>					Adds the player to the queue.");
					System.out.println("remove <name>             		 	Removes the player from the queue.");
					System.out.println("find 						Finds the teams.");
					System.out.println("stop 						Stops the program.");
					System.out.println("clear 						Clears the queue.");
					System.out.println("push 						Pushs the match to all the clients with the selected IP.");
					System.out.println("choose <match-id>          			Adds the selected match to the deck for pushing to the server.");
					System.out.println("tol <#> 					Sets the tolerance level for match skill differences.");
					System.out.println("setip 						Sets the IP / message to push to clients.");
					System.out.println("dat 						Outputs all information in the database.");
					System.out.println("team <#> <identifier        Sets the identifiers for team 1 and team 2.");
					System.out.println("serverip <ip>              		 	Sets the local IP address of the server for pushing data to. Default 192.168.0.2.");
					System.out.println("serverport <port>          			Sets the port on which the server is listening for data. Default: 52000.");
					System.out.println("display 					Sets the number of viable matches to display. -1 for default.");
					System.out.println("entry add <name> <skill> 			Adds a player to the database (overwrites existing). Skill should be between 0-1000.");
					System.out.println("reset 						Resets all settings to the default.");
				} else if(input.startsWith("add")) {
					if(!addPlayer(input.split("\\s+")[1])) {
						System.out.println("Player was not added to queue. Not found in database.");
					}
				} else if(input.startsWith("team")) {
					if(Integer.parseInt(input.split("\\s+")[1]) == 1) team1 = input.split("\\s+")[2];
					if(Integer.parseInt(input.split("\\s+")[1]) == 2) team2 = input.split("\\s+")[2];
					System.out.println("Identifier set successfully.");
				}
				else if(input.startsWith("choose")) {
					for(int i = 0; i < matches.size(); i++) {
						if(matches.get(i).ID == Integer.parseInt(input.split("\\s+")[1])) {
							onDeck = matches.get(i);
							System.out.println("Match ID #"+onDeck.ID+" is now on deck to be pushed.");
						}
					}
					if(onDeck == null) System.out.println("No match was found with ID: "+input.split("\\s+")[1]);
				}
				
				else if(input.startsWith("serverport")) {
					serverPort = Integer.parseInt(input.split("\\s+")[1]);
					System.out.println("Server port set to: "+serverPort);
				}
				else if(input.startsWith("serverip")) { 
					serverIP = input.split("\\s+")[1];
					System.out.println("Server IP set to: "+serverIP);
				} else if(input.startsWith("push")) {
					// client knows that 1 is t, enything else is ct
					if(onDeck == null) System.out.println("You haven't selected a match yet.");
					else {
						String push = "push,"+save.getMessage()+","+(onDeck.t1_players.size()+onDeck.t2_players.size())+",";
						for(int i = 0; i < onDeck.t1_players.size(); i++) {
							push+=onDeck.t1_players.get(i)+":1,";
						}
						for(int i = 0; i < onDeck.t2_players.size(); i++) {
							push+=onDeck.t2_players.get(i)+":2,";
						}
						push += "|";
						System.out.println("Server responded: "+push(push));
					}
				} else if(input.startsWith("quit")) {
					System.out.println("Server responded: "+push("quit2609|"));
				} else if(input.startsWith("stop")) {
					System.out.println("Stopping app...");
					running = false;
					System.exit(0);
				} else if(input.startsWith("find")) {
					System.out.println("Finding teams...");
					matches = MatchFinder.find(queue, save.getTolerance(), save.getDisplay());
				} else if(input.startsWith("tol")) {
					save.setTolerance(Integer.parseInt(input.split("\\s+")[1]));
					System.out.println("Tolerance set to "+save.getTolerance()+".");
				} else if(input.startsWith("setip")) {
					save.setMessage(input.split("\\s+")[1]);
				} else if(input.startsWith("dat")) {
					System.out.println("LOCAL DATABASE: ");
					for(int i = 0; i < save.getDatabase().size(); i++) {
						System.out.println(save.getDatabase().get(i));
					}
				} else if(input.startsWith("remove")) {
					boolean found = false;
					for(int i = 0; i < queue.size(); i++) {
						if(queue.get(i).contains(input.split("\\s+")[1])) {
							queue.remove(i);
							System.out.println("Player removed from queue.");
							found = true;
							break;
						}
					}
					if(!found) {
						System.out.println("Player is not in queue.");
					}
					
				} else if(input.startsWith("queue")) {
					System.out.println("CURRENT QUEUE: ");
					for(int i = 0; i < queue.size(); i++) {
						System.out.println(queue.get(i));
					}
				} else if(input.startsWith("display")) {
					save.setDisplay(Integer.parseInt(input.split("\\s+")[1]));
					System.out.println("Number of viable matches to display set to "+save.getDisplay()+".");
				} else if(input.startsWith("entry add")) {
					boolean found = false;
					if(save != null && save.getDatabase() != null) {
						for (int i = 0; i < save.getDatabase().size(); i++) {
							if(input.split("\\s+")[2].equalsIgnoreCase(save.getDatabase().get(i).split(":")[0])) {
								System.out.println("Entry already exists. Overwriting.");
								ArrayList<String> temp = save.getDatabase();
								temp.set(i, input.split("\\s+")[2] + ":" + input.split("\\s+")[3]);
								found = true;
							}
						}
					} 
					if(!found) {
						save.addDatabaseEntry(input.split("\\s+")[2]+":"+input.split("\\s+")[3]);
						System.out.println("Added entry: "+input.split("\\s+")[2]+":"+input.split("\\s+")[3]+" to the database.");
					}
				} else if(input.startsWith("reset")) {
					Prefs.deleteSave();
					System.out.println("Preferences reset.");
				}
				else if(input.startsWith("clear")) {
					queue.clear();
					System.out.println("Queue cleared.");
				}
			} catch (Exception e) {
				System.out.println("Unrecognized command or incorrect syntax");
			}
			Prefs.serializeSave(save);
			
		}
		scanner.close();
	}

	/**
	 * Pushes a command to the server
	 * @param data The command to send
	 * @return The server's response
	 */
	public String push(String data) {
		try {
			Socket headSocket = new Socket(serverIP, serverPort);
			DataOutputStream stream = new DataOutputStream(headSocket.getOutputStream());
			InputStream in = headSocket.getInputStream();
			stream.writeBytes(data);
			String response = "";
			while (true) {
				int ch = in.read();
				if((ch < 0) || (ch == '|')) {
					break;
				}
				response += (char) ch;
			}
			headSocket.close();
			return response;
		} catch (Exception e) {
			System.err.println("Failed to push data to server");
			e.printStackTrace();
			return "Server did not respond";
		}
	}
	
	public static void main(String[] args) {
		new MainV2();
	}

	/**
	 * Adds a player to the queue.
	 * @param name The player's name as specified in the database file.
	 * @return True if player was added successfully
	 */
	private boolean addPlayer(String name) {
		for(int i = 0; i < database.size(); i++) {
			if(database.get(i).split(":")[0].equalsIgnoreCase(name)) {
				queue.add(database.get(i));
				System.out.println("Player "+name+" added to the queue. "+queue.size()+" Player(s) in queue.");
				return true;
			}
		}
		return false;
	}
}