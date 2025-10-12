package entityutils;


import packets.TileMap;

import static enums.Direction.*;

public class CollisionUtil {
	public static boolean canMove(Entity entity) {
		int tileSize = gamePanel.tileSize;
		TileMap tileMap = gamePanel.tileHandler.getTileMap();
		boolean[][] collisionMap = tileMap.getCollisionMap();
		
		int entityLeftWorldX = entity.worldX + entity.collisionBox.x;
		int entityRightWorldX = entity.worldX + entity.collisionBox.x + entity.collisionBox.width;
		int entityTopWorldY = entity.worldY + entity.collisionBox.y;
		int entityBottomWorldY = entity.worldY + entity.collisionBox.y + entity.collisionBox.height;
		
		int entityLeftCol = entityLeftWorldX/tileSize;
		int entityRightCol = entityRightWorldX/tileSize;
		int entityTopRow = entityTopWorldY/tileSize;
		int entityBottomRow = entityBottomWorldY/tileSize;

		boolean collider1, collider2;
		
		if(entity.direction == UP) {
			entityTopRow = (entityTopWorldY - entity.speed)/tileSize;
			collider1 = collisionMap[entityTopRow][entityLeftCol];
			collider2 = collisionMap[entityTopRow][entityRightCol];
			if(collider1 || collider2) { return false; }
		}
		else if(entity.direction == DOWN){
			entityBottomRow = (entityBottomWorldY + entity.speed)/tileSize;
			collider1 = collisionMap[entityBottomRow][entityLeftCol];
			collider2 = collisionMap[entityBottomRow][entityRightCol];
			if(collider1 || collider2) { return false; }	
		}
		else if(entity.direction == LEFT){
			entityLeftCol = (entityLeftWorldX - entity.speed)/tileSize;
			collider1 = collisionMap[entityTopRow][entityLeftCol];
			collider2 = collisionMap[entityBottomRow][entityLeftCol];
			if(collider1 || collider2) { return false; }
		}
		else if(entity.direction == RIGHT){
			entityRightCol = (entityRightWorldX + entity.speed)/tileSize;
			collider1 = collisionMap[entityTopRow][entityRightCol];
			collider2 = collisionMap[entityBottomRow][entityRightCol];
			if(collider1 || collider2) { return false; }
		} 
		return true;
	}
}
