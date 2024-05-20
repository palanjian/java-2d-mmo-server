package packets;

import enums.Direction;

import java.io.Serializable;

public class PlayerInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int playerX;
	private int playerY;
	private int id;
	private Direction direction;
	private int animState;
	private byte[] spritesheet;
	private boolean online;
	private String username;

	public PlayerInfo(int id, String username, int playerX, int playerY, Direction direction, int animState, byte[] spritesheet) {
		this.id = id;
		this.username = username;
		this.playerX = playerX;
		this.playerY = playerY;
		this.direction = direction;
		this.spritesheet = spritesheet;
		this.setAnimState(animState);
		this.setOnline(true);
	}
	
	public void updatePosition(int playerX, int playerY, Direction direction, int animState) {
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

	public Direction getDirection() { return direction; }

	public void setDirection(Direction direction) { this.direction = direction; }

	public byte[] getSpritesheet() { return spritesheet; }

	public void setSpritesheet(byte[] spritesheet) { this.spritesheet = spritesheet; }

	public int getAnimState() { return animState; }

	public void setAnimState(int animState) { this.animState = animState; }

	public boolean getOnline() { return online; }

	public void setOnline(boolean online) { this.online = online; }

	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }

}