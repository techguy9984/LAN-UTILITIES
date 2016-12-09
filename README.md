# LAN-UTILITIES
An utility for managing LAN parties easily and better.

# Team finding
The LAN-UTILITIES contains a player database. The player database specifies the people that you know at your LAN party
and their approximate skill (0-1000). When ready, the head LAN chief will add all players that desire to play in the next
game to the queue. They can then run the find match command. It will find every single possible combination of teams.
Then, it will narrow them down by a tolerance level. The tolerance level is the difference between team scores to accept. For
example, team 1's total skill level might be 1500  and team's 2 total skill level might be 2000. For this match to be considered,
the minimum tolerance level would have to be 500. Then, a match is chosen randomly from all the viable matches.

# Reporting it to the LAN party
The LAN-UTILTIES contains three applications. The command-line controller application, the server-hub command-line application, and the
client GUI application. Basically, when the controller finds a match that they deem is good, they can push the match to all the 
player's computer. It will display text such as "Your team is: Counter terroist". You can also push the server IP so they aren't
questioning what the server IP is constantly. Whew.

# Commands
*```add <name>``` Adds a player to the queue. The player must have a database profile.  
*```find``` Takes the current queue and finds a viable match.  
*```stop``` Closes the program.  
*```clear``` Clears the queue.  
*```push``` Pushs the current teams to all the clients.  
*```entry add <name> <skill>``` Adds a player to the database, if they are already specified in the database,   
it will overwrite their profile. Skill must be between 0-1000.  
*```tol <#>``` Sets the tolerance level for total score differences.  
*```setip <>``` Sets the IP / message to push to all clients.  
*```reset``` removes all settings  
*```death to all``` Shuts down everybody's computer. Just kidding.  

# Updates
The program will eventually take into account more detailed player profiles to more accuratly depict skill levels.
The program might include a better server control command-line.

# Other
Contact Will Davies at wdavies973@gmail.com to submit bugs, or make suggestions.
