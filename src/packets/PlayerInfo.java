package packets;

import java.io.Serializable;

public class PlayerInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int playerX;
	private int playerY;
	private int id;
	private String direction;
	private int animState;
	private byte[] spritesheet;
	private boolean online;
	
	public PlayerInfo(int id, int playerX, int playerY, String direction, int animState, byte[] spritesheet) {
		this.id = id;
		this.playerX = playerX;
		this.playerY = playerY;
		this.direction = direction;
		this.spritesheet = spritesheet;
		this.setAnimState(animState);
		this.setOnline(true);
	}
	
	public void updatePosition(int playerX, int playerY, String direction, int animState) {
		this.playerX = playerX;
		this.playerY = playerY;
		this.direction = direction;
		this.setAnimState(animState);
	}

	//Getters & Setters
	public int getPlayerX() { return playerX; }

	public void setPlayerX(int playerX) { this.playerX = playerX; }

	public int getPlayerY() { return playerY; }

	public void setPlayerY(int playerY) { this.playerY = playerY; }

	public int getId() { return id; }

	public void setId(int id) { this.id = id; }

	public String getDirection() { return direction; }

	public void setDirection(String direction) { this.direction = direction; }

	public byte[] getSpritesheet() { return spritesheet; }

	public void setSpritesheet(byte[] spritesheet) { this.spritesheet = spritesheet; }

	public int getAnimState() { return animState; }

	public void setAnimState(int animState) { this.animState = animState; }

	public boolean getOnline() { return online; }

	public void setOnline(boolean online) { this.online = online; }
}
