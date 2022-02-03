package com.dethemium.simpletextfilter.filters;

import com.dethemium.simpletextfilter.SimpleTextFilter;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.logging.Level;

public class AnvilFilter implements Listener {

    private final SimpleTextFilter simpleTextFilter;

    public AnvilFilter(SimpleTextFilter simpleTextFilter) {
        this.simpleTextFilter = simpleTextFilter;
    }

    public boolean anvilNeedsFiltering(String itemName) {
        return simpleTextFilter.globalFilterRegex.matcher(itemName).find();
    }

    public ItemMeta filterAnvil(HumanEntity viewer, ItemMeta itemMeta) {
        if (simpleTextFilter.logToConsole) {
            simpleTextFilter.getLogger().log(Level.WARNING, String.format(simpleTextFilter.logToConsoleMessage, viewer.getName(), "anvil", itemMeta.getDisplayName()));
        }
        if (simpleTextFilter.logToPlayer) {
            viewer.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(simpleTextFilter.logToPlayerMessage, "anvil")));
        }
        itemMeta.setDisplayName(itemMeta.getDisplayName().replaceAll(simpleTextFilter.globalFilterRegex.pattern(), ""));
        return itemMeta;
    }

    @EventHandler
    public void playerUseAnvilClick(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof AnvilInventory) {
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                ItemStack itemStack = event.getCurrentItem();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    if (anvilNeedsFiltering(itemMeta.getDisplayName())) {
                        itemStack.setItemMeta(filterAnvil(event.getWhoClicked(), itemMeta));
                    }
                }
            }
        }
    }
}
