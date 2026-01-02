package fr.kisai.deathswap;

import fr.kisai.deathswap.commands.DeathSwapCommands;
import fr.kisai.deathswap.events.EventsListeners;
import fr.kisai.deathswap.game.GameManager;
import fr.kisai.deathswap.game.kits.KitsManager;
import fr.kisai.deathswap.utils.EffectUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    private static Main instance;
    private static GameManager gameManager;
    private KitsManager kitsManager;
    @Override
    public void onEnable() {
        instance = this;
        gameManager = new GameManager(this);
        getCommand("deathswap").setExecutor(new DeathSwapCommands());

        File kitsFile = new File(getDataFolder(), "kits.yml");
        if (!kitsFile.exists()) {
            saveResource("kits.yml", false);
        }

        kitsManager = new KitsManager();
        kitsManager.loadKits(kitsFile);
        registerListeners();
        saveDefaultConfig();
    }



    public void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EventsListeners(),this);
        pm.registerEvents(new EffectUtils(),this);
    }

    public static Main getInstance() {
        return instance;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public KitsManager getKitsManager() {
        return kitsManager;
    }
}