package entities;

import entityutils.Pathfinder;
import enums.Constants;
import enums.Direction;
import temp.GraphicsUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static enums.Direction.*;

public class Entity {

    public int worldX, worldY;
    public int speed;
    public Direction direction;
    public Rectangle collisionBox;
    public BufferedImage[] spriteArray;

    public Entity(int worldX, int worldY){
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = 3;
        this.direction = DOWN;
        collisionBox = new Rectangle();
        collisionBox.x = 16;
        collisionBox.y = 24;
        collisionBox.height = 32;
        collisionBox.width = 32;
        spriteArray = null;

    }
    public void loadSprite(String skinFileName){
        spriteArray = GraphicsUtil.getSpriteArray(GraphicsUtil.loadImage(skinFileName), 4, 4, Constants.ORIGINAL_TILE_SIZE);
    }

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + collisionBox.x)/Constants.TILE_SIZE;
        int startRow = (worldY + collisionBox.y)/Constants.TILE_SIZE;

        Pathfinder pathfinder = gamePanel.pathfinder;
        if(pathfinder == null) return; //quick fix to pathFinder not being instantiated before tilemap
        //is sent from server, causing Null error
        pathfinder.setNodes(startCol, startRow, goalCol, goalRow);

        if(pathfinder.search() == true) {

            //next worldX and worldY
            int nextX = gamePanel.pathfinder.pathList.get(0).col * Constants.TILE_SIZE;
            int nextY = gamePanel.pathfinder.pathList.get(0).row * Constants.TILE_SIZE;

            //entity's solidarea position
            int enLeftX = worldX + collisionBox.x;
            int enRightX = worldX + collisionBox.x + collisionBox.width;
            int enTopY = worldY + collisionBox.y;
            int enBottomY = worldY + collisionBox.y + collisionBox.height;

            //based on entity's current position, find out the relative direction of the next node
            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + Constants.TILE_SIZE) {
                direction = UP;
            }
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + Constants.TILE_SIZE) {
                direction = DOWN;
            }
            else if(enTopY >= nextY && enBottomY < nextY + Constants.TILE_SIZE) {
                //left or right
                if(enLeftX > nextX) direction = LEFT;
                if(enLeftX < nextX) direction = RIGHT;
            }
            else if(enTopY > nextY && enLeftX > nextX) {
                //up or left
                direction = UP;
                if(!CollisionUtil.canMove(gamePanel, this)) direction = LEFT;
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                //up or right
                direction = UP;
                if(!CollisionUtil.canMove(gamePanel, this)) direction = RIGHT;
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                //down or left
                direction = DOWN;
                if(!CollisionUtil.canMove(gamePanel, this)) direction = LEFT;
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                //down or right
                direction = DOWN;
                if(!CollisionUtil.canMove(gamePanel, this)) direction = RIGHT;
            }
        }
}
