package worlds;

import packets.TileMap;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TempWorldHandler {

    public static Map<String, World> initializeAllWorlds(){
        Map<String, World> map = new HashMap<>();

        InputStream is = TempWorldHandler.class.getResourceAsStream("/maps/island.txt");
        World world1 = new World("world1", new TileMap(is));
        TeleportPoint teleportPoint1 = new TeleportPoint("world1", 21, 23, "indoor", 1, 8);
        world1.addTeleportPoint(teleportPoint1);
        map.put(world1.getName(), world1);


        //instead you can do is = getClass().get.....
        is = TempWorldHandler.class.getResourceAsStream("/maps/indoor.txt");
        World world2 = new World("indoor", new TileMap(is));
        TeleportPoint teleportPoint2 = new TeleportPoint("indoor", 0, 2, "world1", 21, 18);
        world2.addTeleportPoint(teleportPoint2);

        map.put(world2.getName(), world2);


        return map;
    }
    public static String getDefaultWorldName(){
        return "world1";
    }
}
