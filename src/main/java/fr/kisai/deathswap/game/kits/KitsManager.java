package fr.kisai.deathswap.game.kits;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class KitsManager {

    private final Map<String, Kit> kits = new HashMap<>();

    public void loadKits(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection section = config.getConfigurationSection("kits");
        if (section == null) return;

        for (String kitName : section.getKeys(false)) {

            List<String> description = section.getStringList(
                    kitName + ".description"
            );

            List<ItemStack> items = new ArrayList<>();

            for (Map<?, ?> itemMap : section.getMapList(kitName + ".items")) {

                Material material = Material.valueOf(
                        itemMap.get("material").toString().toUpperCase()
                );

                int amount = 1;
                Object amountObj = itemMap.get("amount");

                if (amountObj instanceof Number) {
                    amount = ((Number) amountObj).intValue();
                }

                items.add(new ItemStack(material, amount));
            }


            kits.put(
                    kitName.toLowerCase(),
                    new Kit(kitName, items, description)
            );
        }
    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    public Collection<Kit> getKits() {
        return kits.values();
    }
}
