package main;

import java.io.Serializable;

public class PlayerInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int playerX;
	private int playerY;
	private String username;
	private int id;
	
	public PlayerInfo(int id, String username, int playerX, int playerY) {
		this.setId(id);
		this.setUsername(username);
		this.setPlayerX(playerX);
		this.setPlayerY(playerY);
	}
	
	public void updatePosition(int playerX, int playerY) {
		this.setPlayerX(playerX);
		this.setPlayerY(playerY);
	}

	//Getters & Setters
	public int getPlayerX() { return playerX; }

	public void setPlayerX(int playerX) { this.playerX = playerX; }

	public int getPlayerY() { return playerY; }

	public void setPlayerY(int playerY) { this.playerY = playerY; }

	public String getUsername() { return username;}

	public void setUsername(String username) { this.username = username; }

	public int getId() { return id; }

	public void setId(int id) { this.id = id; }
	
}
