package fr.kisai.deathswap.utils;

import fr.kisai.deathswap.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.UUID;

public class EffectUtils implements Listener {
    private static HashMap<UUID, Boolean> nofall = new HashMap<>();


    public static void addNofall(Player player){
        nofall.put(player.getUniqueId(), true);
    }

    public static void removeNofall(Player player){
        nofall.put(player.getUniqueId(), false);
    }


    public static void addTemporaryNoFall(Player player, int time) {
        nofall.put(player.getUniqueId(), true);
        new BukkitRunnable() {
            @Override
            public void run() {
                nofall.put(player.getUniqueId(), false);
            }
        }.runTaskLater(Main.getInstance(), time * 20L);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Player victim = (Player) e.getEntity();
            if (nofall.containsKey(victim.getUniqueId()) && nofall.get(victim.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
}