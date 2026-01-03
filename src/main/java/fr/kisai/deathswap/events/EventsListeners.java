package fr.kisai.deathswap.events;

import fr.kisai.deathswap.Main;
import fr.kisai.deathswap.game.player.GPlayer;
import fr.kisai.deathswap.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsListeners implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        if (!Main.getGameManager().isGame()){
            player.teleport(Main.getGameManager().getLobbyLocation());
            e.setJoinMessage(ChatUtil.translate("&7[&a+&7] &f" + player.getName()));
        } else {
            player.teleport(GPlayer.get(player).getOldLoc());
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        if (Main.getGameManager().isGame()){
            GPlayer.get(player).setOldLoc(player.getLocation());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        e.setDeathMessage(null);

        if (!Main.getGameManager().isGame()) return;

        GPlayer gPlayer = GPlayer.get(player);
        if (!gPlayer.isInGame()) return;

        player.spigot().respawn();
        player.setGameMode(GameMode.SPECTATOR);

        Bukkit.broadcastMessage(ChatUtil.prefix("&c" + player.getName() + " est éliminé !"));

        Main.getGameManager().finishGame(player);
    }

    @EventHandler
    public void onSpectatorMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (player.getGameMode() != GameMode.SPECTATOR) return;
        World world = Bukkit.getWorld("game");
        WorldBorder border = world.getWorldBorder();
        Location to = e.getTo();
        Location center = border.getCenter();

        double size = border.getSize() / 2.0;

        double minX = center.getX() - size;
        double maxX = center.getX() + size;
        double minZ = center.getZ() - size;
        double maxZ = center.getZ() + size;

        if (to.getX() < minX || to.getX() > maxX || to.getZ() < minZ || to.getZ() > maxZ) {

            Location safe = center.clone();
            safe.setY(world.getHighestBlockYAt(center) + 2);

            player.teleport(safe);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (!Main.getGameManager().isGame()){
            e.setCancelled(true);
        } else {
            e.setCancelled(false);
        }
    }
    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent e){
        if (!Main.getGameManager().isGame()){
            e.setCancelled(true);
        } else {
            e.setCancelled(false);
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (!Main.getGameManager().isGame()){
            e.setCancelled(true);
        } else {
            e.setCancelled(false);
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (!Main.getGameManager().isGame()){
            e.setCancelled(true);
        } else {
            e.setCancelled(false);
        }
    }
}