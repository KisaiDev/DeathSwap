package fr.kisai.deathswap.game.player;

import fr.kisai.deathswap.game.kits.Kit;
import fr.kisai.deathswap.game.kits.KitsManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GPlayer {
    public static final HashMap<UUID, GPlayer> players = new HashMap<>();
    private final UUID uniqueId;
    private final String name;
    private Kit kits;
    private boolean ig;
    private World gameWorld;
    private Location oldLoc;

    public GPlayer(Player player) {
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();
        ig = false;
        gameWorld = null;
        oldLoc = null;

        players.put(player.getUniqueId(), this);
    }

    public static GPlayer get(Player player) {
        if (players.containsKey(player.getUniqueId())) return players.get(player.getUniqueId());
        return new GPlayer(player);
    }

    public static GPlayer get(UUID uniqueId) {
        if (players.containsKey(uniqueId)) return players.get(uniqueId);
        return null;
    }

    public void setKits(Kit kits) {
        this.kits = kits;
    }

    public Kit getKits() {
        return kits;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public boolean isInGame() {
        return ig;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public Location getOldLoc() {
        return oldLoc;
    }

    public void setInGame(boolean ig) {
        this.ig = ig;
    }

    public void setGameWorld(World gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void setOldLoc(Location oldLoc) {
        this.oldLoc = oldLoc;
    }
}