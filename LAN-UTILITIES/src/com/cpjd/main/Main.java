package com.cpjd.main;

import java.util.ArrayList;
import java.util.Scanner;

import com.cpjd.main.Prefs.Save;

public class Main {
	
	private ArrayList<String> queue = new ArrayList<String>();
	private ArrayList<String> database;
	
	private boolean running;
	
	public Main() {
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
					System.out.println("tol <#> 					Sets the tolerance level for match skill differences.");
					System.out.println("setip 						Sets the IP / message to push to clients.");
					System.out.println("dat 						Outputs all information in the database.");
					System.out.println("display 					Sets the number of viable matches to display. -1 for default.");
					System.out.println("entry add <name> <skill> 			Adds a player to the database (overwrites existing). Skill should be between 0-1000.");
					System.out.println("reset 						Resets all settings to the default.");
				} else if(input.startsWith("add")) {
					if(!addPlayer(input.split("\\s+")[1])) {
						System.out.println("Player was not added to queue. Not found in database.");
					}
				} else if(input.startsWith("stop")) {
					System.out.println("Stopping app...");
					running = false;
					System.exit(0);
				} else if(input.startsWith("find")) {
					System.out.println("Finding teams...");
					MatchFinder.find(queue, save.getTolerance(), save.getDisplay());
				} else if(input.startsWith("push")) {
					
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

	public static void main(String[] args) {
		new Main();
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