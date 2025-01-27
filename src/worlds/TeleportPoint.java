package worlds;

public class TeleportPoint {

    public String originWorld;
    public String destinationWorld;
    public int originX, originY, destinationX, destinationY;

    public TeleportPoint(String originWorld, int originX, int originY, String destinationWorld, int destinationX, int destinationY){
        this.originWorld = originWorld;
        this.originX = originX;
        this.originY = originY;
        this.destinationWorld = destinationWorld;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }

    public String getTeleportKey(){
        return originX + ":" + originY;
    }

}
