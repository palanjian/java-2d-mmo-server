package worlds;

import main.Player;
import packets.EntityInfo;
import packets.TileMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class World {
    private String name;
    private TileMap tileMap;
    //is HashSet threadsafe? Don't think so
    private Set<Integer> playersInWorld;
    private Set<Integer> entitiesInWorld;
    private Set<Player> playerThreadsInWorld;

    //maps teleportKey to teleportpoint
    private Map<String, TeleportPoint> teleportPoints;

    public World(String name, TileMap defaultTileMap){
        this.name = name;
        tileMap = defaultTileMap;
        playersInWorld = new HashSet<>();
        entitiesInWorld = new HashSet<>();
        playerThreadsInWorld = new HashSet<>();
        teleportPoints = new HashMap<>();
    }

    public TeleportPoint getTeleportPoint(int x, int y){
        String id = x + ":" + y;
        if(teleportPoints.get(id) == null) return null;
        return teleportPoints.get(id);
    }

    public String getName() { return name; }

    public Set<Integer> getPlayersInWorld(){ return playersInWorld; }
    public Set<Integer> getEntitiesInWorld(){ return entitiesInWorld; }

    public TileMap getTileMap(){ return tileMap; }

    public void addPlayerThread(Player p) { playerThreadsInWorld.add(p); }
    public void removePlayerThread(Player p) { playerThreadsInWorld.remove(p); }

    public Set<Player> getPlayerThreads() { return playerThreadsInWorld; }

    public void setTileMap(TileMap tileMap) { this.tileMap = tileMap; }

    public void addPlayerId(int id) { playersInWorld.add(id); }
    public void addEntityId(int id) { entitiesInWorld.add(id); }

    public void removePlayerId(int id) { playersInWorld.remove(id); }
    public void removeEntityId(int id) { entitiesInWorld.remove(id); }
    public void addTeleportPoint(TeleportPoint tp){
        teleportPoints.put(tp.getTeleportKey(), tp);
    }
}
