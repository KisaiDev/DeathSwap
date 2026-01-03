package fr.kisai.deathswap.game.world;

import fr.kisai.deathswap.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;

public class GameWorldsCreator {

    public void create(){
        World world = new WorldCreator("game").createWorld();
        world.getWorldBorder().setCenter(new Location(world,0,100,0));
        world.getWorldBorder().setSize(Main.getGameManager().getBorder());
        world.setPVP(false);
        removeOceanBiomes(world);
        Main.getGameManager().setGameWorld(world);
    }
    private void removeOceanBiomes(World world) {
        WorldBorder border = world.getWorldBorder();
        Location center = border.getCenter();
        int radius = (int) (border.getSize() / 2);

        for (int x = -radius; x <= radius; x += 16) {
            for (int z = -radius; z <= radius; z += 16) {

                int bx = center.getBlockX() + x;
                int bz = center.getBlockZ() + z;

                Biome biome = world.getBiome(bx, bz);

                if (biome == Biome.OCEAN ||
                        biome == Biome.DEEP_OCEAN ||
                        biome == Biome.FROZEN_OCEAN) {

                    for (int dx = 0; dx < 16; dx++) {
                        for (int dz = 0; dz < 16; dz++) {
                            world.setBiome(bx + dx, bz + dz, Biome.PLAINS);
                        }
                    }
                }
            }
        }
    }

}