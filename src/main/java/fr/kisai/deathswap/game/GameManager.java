package fr.kisai.deathswap.game;

import fr.kisai.deathswap.Main;
import fr.kisai.deathswap.game.kits.Kit;
import fr.kisai.deathswap.game.player.GPlayer;
import fr.kisai.deathswap.game.task.GameTask;
import fr.kisai.deathswap.game.world.GameWorldsCreator;
import fr.kisai.deathswap.utils.ChatUtil;
import fr.kisai.deathswap.utils.EffectUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

        giveKitOrRandom(p1);
        giveKitOrRandom(p2);

        EffectUtils.addTemporaryNoFall(p1, 5);
        EffectUtils.addTemporaryNoFall(p2, 5);

        game = true;

        gameTask = new GameTask();

        Bukkit.broadcastMessage(ChatUtil.prefix("&aLa partie commence !"));
    }

    public void startThree(Player p1, Player p2,Player p3) {
        if (game) return;

        GameWorldsCreator creator = new GameWorldsCreator();
        creator.create();
        this.gameWorld = Bukkit.getWorld("game");

        p1.teleport(getSafeRandomLoc());
        p2.teleport(getSafeRandomLoc());
        p3.teleport(getSafeRandomLoc());

        GPlayer.get(p1).setInGame(true);
        GPlayer.get(p2).setInGame(true);
        GPlayer.get(p3).setInGame(true);

        giveKitOrRandom(p1);
        giveKitOrRandom(p2);
        giveKitOrRandom(p3);


        EffectUtils.addTemporaryNoFall(p1, 5);
        EffectUtils.addTemporaryNoFall(p2, 5);
        EffectUtils.addTemporaryNoFall(p3, 5);

        game = true;

        gameTask = new GameTask();

        Bukkit.broadcastMessage(ChatUtil.prefix("&aLa partie commence !"));
    }

    public void giveKitOrRandom(Player player) {
        GPlayer gPlayer = GPlayer.get(player);

        Collection<Kit> allKits = Main.getInstance().getKitsManager().getKits();

        if (gPlayer.getKits() == null) {
            List<Kit> kitList = new ArrayList<>(allKits);

            Kit randomKit = kitList.get(new Random().nextInt(kitList.size()));
            gPlayer.setKits(randomKit);

            player.sendMessage(ChatUtil.prefix("&eVous n'aviez pas choisi de kit, vous recevez le kit : &f" + randomKit.getName()));
        }

        Kit kit = gPlayer.getKits();
        for (ItemStack item : kit.getItems()) {
            player.getInventory().addItem(item);
        }
    }

    public void safeStopGame() {
        stopGame();

        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatUtil.prefix("&c&lLa partie a été arrêtée."));
        Bukkit.broadcastMessage(" ");
        deleteGameWorld();
    }
    public void stopGame(){
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
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
    }

    public void finishGame(Player victim) {
        if (!game) return;

        GPlayer.get(victim).setInGame(false);

        List<Player> alivePlayers = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (GPlayer.get(p).isInGame()) {
                alivePlayers.add(p);
            }
        }

        if (alivePlayers.size() > 1) {
            Bukkit.broadcastMessage(ChatUtil.prefix("&c" + victim.getName() + " est éliminé !"));
            return;
        }


        if (alivePlayers.size() == 1) {
            Player winner = alivePlayers.get(0);

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatUtil.prefix("&6&lVictoire de &e" + winner.getName()));
            Bukkit.broadcastMessage(" ");
        }

        stopGame();
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