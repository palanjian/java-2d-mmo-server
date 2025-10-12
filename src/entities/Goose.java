package entities;

public class Goose extends Entity {
    private final String skinFileName = "entities/GOOSE_SPRITESHEET.png";
    public Goose(int worldX, int worldY){
        super(worldX, worldY);
        loadSprite(skinFileName);
    }

}
