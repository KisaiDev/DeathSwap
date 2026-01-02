package fr.kisai.deathswap.events;

import fr.kisai.deathswap.Main;
import fr.kisai.deathswap.game.player.GPlayer;
import fr.kisai.deathswap.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
    public void onDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        e.setDeathMessage(null);
        if (Main.getGameManager().isGame()){
            Main.getGameManager().finishGame(player);
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