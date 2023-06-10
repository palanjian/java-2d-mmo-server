package main;

import java.io.InputStream;
import java.util.Scanner;
import packets.TileMap;

public class CommandHandler extends Thread{
	
	private Server server;
	public CommandHandler(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		Scanner scan = new Scanner(System.in);
		while(true) {
			String line = scan.nextLine().strip().toLowerCase();
			String arguments[] = line.split(" ");
			if(arguments[0].equals("changemap")) {
		        try {
		        	InputStream is = getClass().getResourceAsStream("/maps/" + arguments[1] + ".txt"); 
		        	TileMap tileMap = new TileMap(is);
		        	server.sendTileMap(tileMap);
		        } catch(Exception e){ System.out.println("Incorrect parameters OR map doesn't exist."); }
			}
			else System.out.println("Unrecognized command.");
		}
	}		
}
