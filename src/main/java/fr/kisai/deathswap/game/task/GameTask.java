package fr.kisai.deathswap.game.task;

import fr.kisai.deathswap.Main;
import fr.kisai.deathswap.game.player.GPlayer;
import fr.kisai.deathswap.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameTask extends BukkitRunnable {

    private int timer;

    public GameTask() {
        this.timer = Main.getGameManager().getTimer();
        this.runTaskTimer(Main.getInstance(), 20, 20);
    }

    @Override
    public void run() {
        timer--;

        List<Player> ingame = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (GPlayer.get(p).isInGame()) {
                ingame.add(p);
                String msg = Main.getInstance().getConfig().getString("timer-message").replace("<time>",String.valueOf(timer));
                Title.sendActionBar(p, "&eTemps avant swap : &c" + timer + "s");
            }
        }

        if (timer <= 0) {
            swapPlayers(ingame);
            timer = Main.getGameManager().getTimer(); // reset
        }
    }

    private void swapPlayers(List<Player> players) {
        if (players.size() < 2) return;

        List<Location> locations = new ArrayList<>();
        for (Player p : players) {
            locations.add(p.getLocation());
        }

        int shift;
        if (players.size() == 2) {
            shift = 1;
        } else {
            shift = new Random().nextInt(players.size() - 1) + 1;
        }

        Collections.rotate(locations, shift);

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            p.teleport(locations.get(i));

            Title.sendTitle(p, "§c⚠ SWAP !", " ", 10);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
        }
    }
}