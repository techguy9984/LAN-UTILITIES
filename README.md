# LAN-UTILITIES
Contains three utilites for making LAN parties easier.

# Summary
The LAN-UTILTIES contains three applications. The command-line controller application, the server-hub command-line application, and the
client GUI application. Basically, when the controller finds a skill-balanced match that they deem is good, they can push the match to all the players' computers. It will display text such as "Your team is: Counter terroist". You can also push the server IP so they aren't
questioning what the server IP is constantly. Whew.

# Controller utility
The controller utility contains a player database. The player database specifies the people that you know at your LAN party
and their approximate skill (0-1000). When ready, the head LAN chief will add all players that desire to play in the next
game to the queue. They can then run the find match command. It will find every single possible combination of teams.
Then, it will narrow them down by a tolerance level. The tolerance level is the difference between team scores to accept. For
example, team 1's total skill level might be 1500  and team's 2 total skill level might be 2000. For this match to be considered,
the minimum tolerance level would have to be 500. Then, a match is chosen randomly from all the viable matches. This match can be pushed to the server.

# Server utility
The server stores the game server IP address and the current match, with every player's name and team (ct or t). 
The server is command-line based.

# Client utility
The client is a GUI-based utility that should be loaded onto every LAN party memeber's computer. The client will automatically
connect to the server and pull the IP and team of the player. So basically, the controller pushes the teams, and every player in the room will receive a notification as to what team they are on, and they are provided with a copy button, that will copy the exact connect command. They can then paste this into the CS:GO console and connect to the game. 

# Controller command-line commands
* ```add <name>``` Adds a player to the queue. The player must have a database profile.  
* ```find``` Takes the current queue and finds a viable match.  
* ```stop``` Closes the program.  
* ```clear``` Clears the queue.  
* ```push``` Pushs the current teams to all the clients.  
* ```entry add <name> <skill>``` Adds a player to the database, if they are already specified in the database,   
it will overwrite their profile. Skill must be between 0-1000.  
* ```tol <#>``` Sets the tolerance level for total score differences.  
* ```setip <>``` Sets the IP / message to push to all clients.  
* ```reset``` removes all settings  
* ```death to all``` Shuts down everybody's computer. Just kidding.  

# Server command-line commands
Just start the server by navigating to its directory with CMD. Then type ```java -jar lan-server.jar```. If you'd like to change the default port, add the new port to the end, such as ```-jar lan-server.jar 4480``` and the server will start on the following port. If client are having difficulty connecting, make sure it's allowed through your firewall.

# Client GUI
The client will display your team and IP, or any other message really. Make sure to enter only your first name in the settings, along with the router IPv4 address for the server (not your external IP address!) and the port that your server is using. The setting will automatically be saved so that when they relaunch - they'll still be there.

# Updates
The program will eventually take into account more detailed player profiles to more accuratly depict skill levels.
The program might include a better server control command-line.

# Other
Contact Will Davies at wdavies973@gmail.com to submit bugs, or make suggestions.
