package fr.kisai.deathswap.game;

import fr.kisai.deathswap.game.player.GPlayer;
import fr.kisai.deathswap.game.task.GameTask;
import fr.kisai.deathswap.game.world.GameWorldsCreator;
import fr.kisai.deathswap.utils.ChatUtil;
import fr.kisai.deathswap.utils.EffectUtils;
import fr.kisai.deathswap.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

public class GameManager {

    private final JavaPlugin plugin;
    private int timer;
    private int border;
    private World gameWorld;
    private boolean game;
    private GameTask gameTask;

    public GameManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.timer = plugin.getConfig().getInt("timer");
        this.border = plugin.getConfig().getInt("border");
        this.game = false;
    }


    public Location getLobbyLocation() {
        return new Location(
                Bukkit.getWorld("world"),
                plugin.getConfig().getDouble("lobby.x"),
                plugin.getConfig().getDouble("lobby.y"),
                plugin.getConfig().getDouble("lobby.z")
        );
    }


    public void startTwo(Player p1, Player p2) {

        if (game) return;

        GameWorldsCreator creator = new GameWorldsCreator();
        creator.create();
        this.gameWorld = Bukkit.getWorld("game");

        p1.teleport(getSafeRandomLoc());
        p2.teleport(getSafeRandomLoc());

        GPlayer.get(p1).setInGame(true);
        GPlayer.get(p2).setInGame(true);
        p1.getInventory().addItem(GPlayer.get(p1).getKits().getItems());
        p2.getInventory().addItem(GPlayer.get(p2).getKits().getItems());
        System.out.println("Player One Kits: " + GPlayer.get(p1).getKits().getName());
        System.out.println("Player Two Kits: " + GPlayer.get(p2).getKits().getName());

        EffectUtils.addTemporaryNoFall(p1, 5);
        EffectUtils.addTemporaryNoFall(p2, 5);

        game = true;

        gameTask = new GameTask();

        Bukkit.broadcastMessage(ChatUtil.prefix("&aLa partie commence !"));
    }


    public void safeStopGame() {
        if (!game) return;

        game = false;

        if (gameTask != null) {
            gameTask.cancel();
            gameTask = null;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (GPlayer.get(p).isInGame()) {

                GPlayer.get(p).setInGame(false);

                p.teleport(getLobbyLocation());
                p.getInventory().clear();
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setExp(0);
                p.setLevel(0);
            }
        }

        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatUtil.prefix("&c&lLa partie a été arrêtée."));
        Bukkit.broadcastMessage(" ");
        deleteGameWorld();
    }


    public void finishGame(Player victim) {
        if (!game) return;

        Player winner = null;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (GPlayer.get(p).isInGame() && !p.equals(victim)) {
                winner = p;
                break;
            }
        }

        if (winner != null) {
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatUtil.prefix("&6&lVictoire de &e" + winner.getName()));
            Bukkit.broadcastMessage(" ");
        }

        safeStopGame();
        deleteGameWorld();
    }

    public Location getSafeRandomLoc() {

        Random random = new Random();
        World w = gameWorld;

        int border = (int) w.getWorldBorder().getSize() - 50;

        int x = random.nextInt(border) - border / 2;
        int z = random.nextInt(border) - border / 2;

        int y = w.getHighestBlockYAt(x, z) + 1;

        return new Location(w, x + 0.5, y, z + 0.5);
    }


    public int getTimer() {
        return timer;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public boolean isGame() {
        return game;
    }

    public void deleteGameWorld() {

        if (gameWorld == null) return;

        for (Player p : gameWorld.getPlayers()) {
            p.teleport(getLobbyLocation());
        }

        Bukkit.unloadWorld(gameWorld, false);

        File worldFolder = gameWorld.getWorldFolder();
        deleteFolder(worldFolder);

        gameWorld = null;
        Bukkit.broadcastMessage(ChatUtil.prefix("&cLe monde de jeu a été supprimé !"));
    }

    private void deleteFolder(File folder) {
        if (!folder.exists()) return;

        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void setGame(boolean game) {
        this.game = game;
    }

    public void setGameWorld(World gameWorld) {
        this.gameWorld = gameWorld;
    }

    public int getBorder() {
        return border;
    }
}