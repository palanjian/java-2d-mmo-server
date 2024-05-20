package packets;

import enums.Direction;

import java.io.Serializable;

public class EntityInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	//can add "type" enum - passive, agro, resource, etc
	private int entityX;
	private int entityY;
	private int id;
	private Direction direction;
	private int animState;
	private byte[] spritesheet;
	private boolean online;

	public EntityInfo(int id, int entityX, int entityY, Direction direction, int animState, byte[] spritesheet) {
		this.id = id;
		this.entityX = entityX;
		this.entityY = entityY;
		this.direction = direction;
		this.spritesheet = spritesheet;
		this.setAnimState(animState);
		this.online = true;
	}
	
	public void updatePosition(int entityX, int entityY, Direction direction, int animState) {
		this.entityX = entityX;
		this.entityY = entityY;
		this.direction = direction;
		this.setAnimState(animState);
	}
	//Getters & Setters
	public int getEntityX() { return entityX; }

	public void setEntityX(int entityX) { this.entityX = entityX; }

	public int getEntityY() { return entityY; }

	public void setEntityY(int entityY) { this.entityY = entityY; }

	public int getId() { return id; }

	public void setId(int id) { this.id = id; }

	public Direction getDirection() { return direction; }

	public void setDirection(Direction direction) { this.direction = direction; }

	public byte[] getSpritesheet() { return spritesheet; }

	public void setSpritesheet(byte[] spritesheet) { this.spritesheet = spritesheet; }

	public boolean getOnline() { return online; }

	public void setOnline(boolean online) { this.online = online; }

	public int getAnimState() { return animState; }

	public void setAnimState(int animState) { this.animState = animState; }

}
