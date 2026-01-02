package fr.kisai.deathswap.game.kits;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {
    private final String name;
    private final List<ItemStack> items;
    private final List<String> description;

    public Kit(String name, List<ItemStack> items, List<String> description) {
        this.name = name;
        this.items = items;
        this.description = description;
    }

    public ItemStack[] getItems() {
        return items.toArray(new ItemStack[0]);
    }

    public String[] getDescription() {
        return description.toArray(new String[0]);
    }

    public String getName() {
        return name;
    }
}
