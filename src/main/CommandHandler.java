package main;

import java.io.InputStream;
import java.util.Scanner;

import packets.ChatMessage;
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
			if(arguments[0].equals("changemap") || arguments[0].equals("cm")) {
		        try {
		        	InputStream is = getClass().getResourceAsStream("/maps/" + arguments[1] + ".txt"); 
		        	TileMap tileMap = new TileMap(is);
		        	server.sendTileMap(tileMap);
		        } catch(Exception e){ System.out.println("Incorrect parameters OR map doesn't exist."); }
			}
			//for testing purposes
			else if(arguments[0].equals("sendmessage") || arguments[0].equals("sm")) {
				try {
					String text = "";
					for(int i=1; i<arguments.length; i++) {
						text += arguments[i] + " ";
					}
					text.strip();
					ChatMessage msg = new ChatMessage(text, "Server");
					server.sendChatMessage(msg);
				} catch(Exception e){ System.out.println("Error sending message."); }
			}
			else System.out.println("Unrecognized command.");
		}
	}		
}
