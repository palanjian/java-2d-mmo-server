package main;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import enums.Direction;
import enums.EntityType;
import packets.ChatMessage;
import packets.EntityPacket;
import packets.TileMap;
import temp.GraphicsUtil;
import worlds.World;

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
					World world = server.getWorld(arguments[1]);
		        	InputStream is = getClass().getResourceAsStream("/maps/" + arguments[2] + ".txt");
		        	TileMap tileMap = new TileMap(is);
		        	server.sendTileMap(tileMap, world);
		        } catch(Exception e){ System.out.println("Incorrect parameters OR map doesn't exist."); }
			}
			//for testing purposes
			else if(arguments[0].equals("sendmessage") || arguments[0].equals("sm")) {
				try {
					World world = server.getWorld(arguments[1]);
					String text = "";
					for(int i=2; i<arguments.length; i++) {
						text += arguments[i] + " ";
					}
					ChatMessage msg = new ChatMessage(text, "Server");
					server.sendChatMessage(msg, world);
				} catch(Exception e){ System.out.println("Error sending message."); }
			}
			//create some random placeholder entity at X, Y
			//usage = se (world_name) (x_pos) (y_pos)
			//x and y are of the form ex 1620 and 1340, not 16 and 14 btw
			else if(arguments[0].equals("sendentity") || arguments[0].equals("se")){
				World world = server.getWorld(arguments[1]);
				if(world == null){
					System.out.println("Unknown world");
					return;
				}
				int x = Integer.parseInt(arguments[2]);
				int y = Integer.parseInt(arguments[3]);
				Random rand = new Random();
				String entitySkinFileName = "COW_SPRITESHEET";
				EntityPacket ep = new EntityPacket(EntityType.RESOURCE, rand.nextInt(), x, y, Direction.DOWN, 0,
						GraphicsUtil.bufferedImageToBytes(GraphicsUtil.loadImage(entitySkinFileName), "PNG"), "Doja");

				server.sendEntityPacketToWorld(ep, world);
			}
			else System.out.println("Unrecognized command.");
		}
	}		
}
