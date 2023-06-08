//NEW FEATURES


//REFACTORS
	- change the nonplayerhandler inputstream code to another class, which will handle players leaving, terrain objects, etc
	- add multiple map functionality in tilehandler by getting rid of hardcoded values
	- add ability for the server to send map updates
	

//BUGS
 - userID implemented, but could be conflicts if it already exists. could do a quick check to server to see if it those, or could just use something that is uncopyable
 - removed player doesn't update client-side
 - I'm 99% sure spriteHandler is backwards: column is row, row is column


//FUTURE IMPLEMENTATION
	- perhaps change nonPlayerHandler to not be a thread, just call update in gamePanel after calling player.update and get the latest location from players using while(exists smt in buffer)
	- when player joins a server they send their unchangeable info (skin, username, userid) and then after they just send the info thats changed (position)
	- only send player skin once. also, only render players that are in view. 

