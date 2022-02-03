package com.dethemium.simpletextfilter.filters;

import com.dethemium.simpletextfilter.SimpleTextFilter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.logging.Level;

public class SignFilter implements Listener {

    private final SimpleTextFilter simpleTextFilter;

    public SignFilter(SimpleTextFilter simpleTextFilter) {
        this.simpleTextFilter = simpleTextFilter;
    }

    public boolean signNeedsFiltering(String[] signTextLines) {
        return Arrays.stream(signTextLines).anyMatch(page -> simpleTextFilter.globalFilterRegex.matcher(page).find());
    }

    public String filterSign(Player player, String signTextLine) {
        String filteredText = signTextLine.replaceAll(simpleTextFilter.globalFilterRegex.pattern(), "");
        if (simpleTextFilter.logToConsole) {
            simpleTextFilter.getLogger().log(Level.WARNING, player.getName() + " had their sign filtered!");
        }
        return filteredText;
    }

    @EventHandler
    public void playerEditSign(SignChangeEvent event) {
        if (signNeedsFiltering(event.getLines())) {
            for (int line = 0; line < event.getLines().length; line++) {
                event.setLine(line, filterSign(event.getPlayer(), event.getLines()[line]));
            }
        }
    }
}
