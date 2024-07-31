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
        map.put(world1.getName(), world1);

        //instead you can do is = getClass().get.....
        is = TempWorldHandler.class.getResourceAsStream("/maps/map01.txt");
        World world2 = new World("world2", new TileMap(is));
        map.put(world2.getName(), world2);

        return map;
    }
    public static String getDefaultWorldName(){
        return "world1";
    }
}
