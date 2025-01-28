package packets;

import enums.Direction;
import enums.EntityType;

import java.io.Serializable;

public class EntityPacket implements Serializable{

	private static final long serialVersionUID = 1L;

	//can add "type" enum - passive, agro, resource, etc

	private EntityType type;
	private int id;
	private int worldX;
	private int worldY;
	private Direction direction;
	private int spriteNumber;
	private byte[] spritesheet;
	private boolean online;
	private String username;

	public EntityPacket(EntityType type, int id, int worldX, int worldY, Direction direction, int spriteNumber, byte[] spritesheet, String username) {
		this.type = type;
		this.id = id;
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.spritesheet = spritesheet;
		this.spriteNumber = spriteNumber;
		this.online = true;
		this.username = username;
	}
	
	public void updatePosition(int worldX, int worldY, Direction direction, int spriteNumber) {
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.spriteNumber = spriteNumber;
	}

	//Getters & Setters
	public int getWorldX() { return worldX; }

	public void setWorldX(int worldX) { this.worldX = worldX; }

	public int getWorldY() { return worldY; }

	public void setWorldY(int worldY) { this.worldY = worldY; }

	public int getId() { return id; }

	public void setId(int id) { this.id = id; }

	public Direction getDirection() { return direction; }

	public void setDirection(Direction direction) { this.direction = direction; }

	public byte[] getSpritesheet() { return spritesheet; }

	public void setSpritesheet(byte[] spritesheet) { this.spritesheet = spritesheet; }

	public boolean getOnline() { return online; }

	public void setOnline(boolean online) { this.online = online; }
	
	public int getSpriteNumber() { return spriteNumber; }

	public void setSpriteNumber(int spriteNumber) { this.spriteNumber = spriteNumber; }

	public EntityType getType() { return type; }

	public void setType(EntityType type) { this.type = type; }

	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }
}
