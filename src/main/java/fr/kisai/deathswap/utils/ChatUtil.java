package fr.kisai.deathswap.utils;

import fr.kisai.deathswap.Main;
import org.bukkit.ChatColor;

public class ChatUtil {

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String prefix(String message) {
        return translate(Main.getInstance().getConfig().getString("prefix") + message);
    }

}
