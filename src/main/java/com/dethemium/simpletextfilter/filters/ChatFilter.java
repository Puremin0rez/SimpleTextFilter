package com.dethemium.simpletextfilter.filters;

import com.dethemium.simpletextfilter.SimpleTextFilter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.logging.Level;

public class ChatFilter implements Listener {

    private final SimpleTextFilter simpleTextFilter;

    public ChatFilter(SimpleTextFilter simpleTextFilter) {
        this.simpleTextFilter = simpleTextFilter;
    }

    public boolean chatNeedsFiltering(String message) {
        return simpleTextFilter.globalFilterRegex.matcher(message).find();
    }

    public String filterChat(Player player, String message) {
        String filteredMessage = message.replaceAll(simpleTextFilter.globalFilterRegex.pattern(), "");
        if (simpleTextFilter.logToConsole) {
            simpleTextFilter.getLogger().log(Level.WARNING, player.getName() + " had their chat filtered!");
        }
        return filteredMessage;
    }

    @EventHandler
    public void playerChatMessage(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        if (chatNeedsFiltering(message)) {
            event.setMessage(filterChat(event.getPlayer(), message));
        }
    }
}
