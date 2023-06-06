package main;

import java.io.Serializable;

public class PlayerInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int playerX;
	private int playerY;
	private int id;
	private String direction;
	
	public PlayerInfo(int id, int playerX, int playerY, String direction) {
		this.id = id;
		this.playerX = playerX;
		this.playerY = playerY;
		this.direction = direction;
	}
	
	public void updatePosition(int playerX, int playerY, String direction) {
		this.setPlayerX(playerX);
		this.setPlayerY(playerY);
		this.setDirection(direction);
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

}
