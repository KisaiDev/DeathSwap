package fr.kisai.deathswap.game.world;

import fr.kisai.deathswap.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class GameWorldsCreator {

    public void create(){
        World world = new WorldCreator("game").createWorld();
        world.getWorldBorder().setCenter(new Location(world,0,100,0));
        world.getWorldBorder().setSize(Main.getGameManager().getBorder());
        Main.getGameManager().setGameWorld(world);
    }
}