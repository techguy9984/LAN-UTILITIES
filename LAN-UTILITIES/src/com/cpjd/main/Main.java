package com.cpjd.main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
	
	// Give them a score out of 1000, 1000 is the best, 0 hasn't played before 
	private final String[] DATABASE = {
			"Isaac:700","Joe:850","Will:800","Jake:750",
			"John:200","Israel:100","Qui:650","Michael:200",
			"IsaacM:600","Daniel:600","Kyle:100","Aaron:100"
	};
	
	private ArrayList<String> QUEUE = new ArrayList<String>();
	
	static boolean running;
	
	public Main() {
		running = true;

		for(int i = 0; i < 10; i++) {
			QUEUE.add(DATABASE[i]);
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the CS:GO Team finder. Type <help> for help.");

		while (running) {
			String input = scanner.nextLine();

			try {
				if (input.equals("help")) {
					System.out.println("Command: add <name>. Adds the player to the queue");
					System.out.println("Command: find. Finds the teams");
					System.out.println("Command: stop. Stops the program.");
					System.out.println("Command: clear. Clears the queue.");
					System.out.println("Command: push. Pushs the match to all the clients with the selected IP.");
				} else if (input.startsWith("add")) {
					if(!addPlayer(input.split("\\s+")[1])) {
						System.out.println("Player was not added to queue. Not found in database.");
					}
				} else if (input.startsWith("stop")) {
					System.out.println("Stopping");
					running = false;
					System.exit(0);
				} else if (input.startsWith("find")) {
					System.out.println("Finding teams...");
					findV2(QUEUE);
				}
				else if(input.startsWith("clear")) {
					QUEUE.clear();
					System.out.println("Queue cleared.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Incorrect syntax");
			}
			
		}
		scanner.close();
	}

	public static void main(String[] args) {
		new Main();
	}

	private boolean addPlayer(String name) {
		for(int i = 0; i < DATABASE.length; i++) {
			if(DATABASE[i].split(":")[0].equals(name)) {
				QUEUE.add(DATABASE[i]);
				System.out.println("Player "+name+" added to the queue. "+QUEUE.size()+" Player(s) in queue.");
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds all the possible teams by using binary counting.
	 * @param queue
	 */
	private void findV2(ArrayList<String> queue) {
		ArrayList<String> chart = new ArrayList<String>();
		
		for(int i = 0; i < Math.pow(2, queue.size()); i++) {
			String s = Integer.toBinaryString(i);
			while(s.length() < queue.size()) {
				s+="0";
			}
			chart.add(s);
		}
		
		ArrayList<Match> matches = new ArrayList<Match>();
		
		// Process the possibilites
		for(int row = 0; row < chart.size(); row++) {
			Match match = new Match();
			for(int col = 0; col < chart.get(row).length(); col++) {
				if(chart.get(row).charAt(col) == '0') {
					match.t1_players.add(queue.get(col).split(":")[0]);
					match.t1_totalSkill += Integer.parseInt(queue.get(col).split(":")[1]);
				} else {
					match.t2_players.add(queue.get(col).split(":")[0]);
					match.t2_totalSkill += Integer.parseInt(queue.get(col).split(":")[1]);
				}
			}
			matches.add(match);
		}

		int tolerance = 50; // the max acceptable score tolerance
		ArrayList<Match> viable = new ArrayList<Match>();
		// Sort the matches
		for(int i = 0; i < matches.size(); i++) {
			if(Math.abs(matches.get(i).t1_totalSkill - matches.get(i).t2_totalSkill) <= tolerance) {
				viable.add(matches.get(i));
			}
		}
		
		// Randomly pick one of them
		Random r = new Random();
		Match selected;
		try {
			 selected = viable.get(r.nextInt(viable.size()));
		} catch(Exception e) {
			System.out.println("Match not found. Try increasing match tolerance.");
			return;
		}
		// Output to the user
		System.out.println("Found a match: ");
		printMatchInfo(selected);
	}
	
	private void printMatchInfo(Match match) {
			System.out.println("Team 1 skill: "+match.t1_totalSkill);
			String t1 = "";
			for(int j = 0; j < match.t1_players.size(); j++) {
				t1 += match.t1_players.get(j)+", ";
			}
			System.out.println(t1);
			System.out.println("Team 2 skill: "+match.t2_totalSkill);
			String t2 = "";
			for(int j = 0; j < match.t2_players.size(); j++) {
				t2 += match.t2_players.get(j)+", ";
			}
			System.out.println(t2);
	}
	/*
	private void printMatchesInfo(ArrayList<Match> matches) {
		for(int i = 0; i < matches.size(); i++) {
			System.out.println("Match "+i);
			System.out.println("Team 1 skill: "+matches.get(i).t1_totalSkill);
			String t1 = "";
			for(int j = 0; j < matches.get(i).t1_players.size(); j++) {
				t1 += "Player "+(j+1)+": "+matches.get(i).t1_players.get(j)+" ";
			}
			System.out.println(t1);
			System.out.println("Team 2 skill: "+matches.get(i).t2_totalSkill);
			String t2 = "";
			for(int j = 0; j < matches.get(i).t2_players.size(); j++) {
				t2 += "Player "+(j+1)+": "+matches.get(i).t2_players.get(j)+" ";
			}
			System.out.println(t2);
		}
	}*/
	
	/*
	 * For the find method here's how it works:
	 * We want to balance the scores on each team so that they are as close as possible.
	 * The score we're aiming for is half of the total points on each side.
	 */
	private void find() {
		System.out.println("QUEUE SIZE: "+QUEUE.size());
		int points = 0;
		for(int i = 0; i < QUEUE.size(); i++) {
			points += Integer.parseInt(QUEUE.get(i).split(":")[1]);
		}
		double perTeam = points / 2;
		System.out.println("Total team points: "+points+", Points per team: "+perTeam);
		
		// Find all possible permutations
		ArrayList<Match> teams = new ArrayList<Match>();
		
		for(int i = 0; i < QUEUE.size(); i++) {
			Match team = new Match();
			team.t1_players.add(QUEUE.get(i));
			for(int j = 0; j < QUEUE.size(); j++) {
				if(j == i) continue;
				team.t1_players.add(QUEUE.get(j));
				for(int k = 0; k < QUEUE.size(); k++) {
					if(k == i || k == j) continue;
					team.t1_players.add(QUEUE.get(k));
					for(int l = 0; l < QUEUE.size(); l++) {
						if(l == k || l == j || l == i) continue;
						team.t1_players.add(QUEUE.get(l));
						for(int m = 0; m < QUEUE.size(); m++) {
							if(m == l || m == k || m == j || m == i) continue;
							team.t1_players.add(QUEUE.get(m));
						}
					}
				}
			}
			for(int j = 0; j < team.t1_players.size(); j++) {
				for(int k = 0; k < QUEUE.size(); k++) {
					if(QUEUE.get(k).split(":")[0].equals(team.t1_players.get(j))) continue;
					team.t2_players.add(QUEUE.get(k).split(":")[0]);
					team.t2_totalSkill += Integer.parseInt(QUEUE.get(k).split(":")[1]);
				}
			}
			teams.add(team);
			QUEUE.remove(0);
		}
		System.out.println("There are "+teams.size()+" possibilites");
		print(teams);
	}
	
	private void print(ArrayList<Match> teams) {
		for(int i = 0; i < teams.size(); i++) {
		}
	}
	
	public class Match {
		public ArrayList<String> t1_players = new ArrayList<String>();
		public int t1_totalSkill = 0;
		
		public ArrayList<String> t2_players = new ArrayList<String>();
		public int t2_totalSkill = 0;
		
	}

}