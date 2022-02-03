package com.dethemium.simpletextfilter.filters;

import com.dethemium.simpletextfilter.SimpleTextFilter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BookFilter implements Listener {

    private final SimpleTextFilter simpleTextFilter;

    public BookFilter(SimpleTextFilter simpleTextFilter) {
        this.simpleTextFilter = simpleTextFilter;
    }

    public boolean bookNeedsFiltering(BookMeta bookMeta) {
        List<String> pages = bookMeta.getPages();
        return pages.stream().anyMatch(page -> simpleTextFilter.globalFilterRegex.matcher(page).find());
    }

    public BookMeta filterBook(Player player, BookMeta bookMeta) {
        List<String> pages = bookMeta.getPages();
        pages = pages.stream().map(page -> page.replaceAll(simpleTextFilter.globalFilterRegex.pattern(), "")).collect(Collectors.toList());
        bookMeta.setPages(pages);
        if (simpleTextFilter.logToConsole) {
            simpleTextFilter.getLogger().log(Level.WARNING, player.getName() + " had their book filtered!");
        }
        return bookMeta;
    }

    @EventHandler
    public void playerEditBook(PlayerEditBookEvent event) {
        BookMeta bookMeta = event.getNewBookMeta();
        if (bookNeedsFiltering(bookMeta)) {
            event.setNewBookMeta(filterBook(event.getPlayer(), bookMeta));
        }
    }
}
