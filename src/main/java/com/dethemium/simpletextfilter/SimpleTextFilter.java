package com.dethemium.simpletextfilter;

import com.dethemium.simpletextfilter.filters.AnvilFilter;
import com.dethemium.simpletextfilter.filters.BookFilter;
import com.dethemium.simpletextfilter.filters.ChatFilter;
import com.dethemium.simpletextfilter.filters.SignFilter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SimpleTextFilter extends JavaPlugin {

    public FileConfiguration config = getConfig();
    public Pattern globalFilterRegex;
    public Boolean logToConsole;
    public Boolean logToPlayer;
    public String logToConsoleMessage;
    public String logToPlayerMessage;
    public Boolean chatFilterEnabled;
    public Boolean signFilterEnabled;
    public Boolean bookFilterEnabled;
    public Boolean anvilFilterEnabled;

    @Override
    public void onEnable() {
        config.addDefault("settings.global.regex-filter", "\\b(ass|apple)");
        config.addDefault("settings.global.log-to-console", Boolean.TRUE);
        config.addDefault("settings.global.log-to-player", Boolean.TRUE);
        config.addDefault("settings.global.messages.log-to-console", "%s had the following %s text filtered: \"%s\"");
        config.addDefault("settings.global.messages.log-to-player", "&cYour %s text has been filtered!");
        config.addDefault("settings.filters.chat.enabled", Boolean.TRUE);
        config.addDefault("settings.filters.sign.enabled", Boolean.TRUE);
        config.addDefault("settings.filters.book.enabled", Boolean.TRUE);
        config.addDefault("settings.filters.anvil.enabled", Boolean.TRUE);
        config.options().copyDefaults(true);
        saveConfig();

        try {
            globalFilterRegex = Pattern.compile(config.getString("settings.global.regex-filter"));
        } catch (PatternSyntaxException ex) {
            getLogger().log(Level.SEVERE, "The global regex filter you have provided is invalid!");
        }
        logToConsole = config.getBoolean("settings.global.log-to-console");
        logToPlayer = config.getBoolean("settings.global.log-to-player");
        logToConsoleMessage = config.getString("settings.global.messages.log-to-console");
        logToPlayerMessage = config.getString("settings.global.messages.log-to-player");
        chatFilterEnabled = config.getBoolean("settings.filters.chat.enabled");
        signFilterEnabled = config.getBoolean("settings.filters.sign.enabled");
        bookFilterEnabled = config.getBoolean("settings.filters.book.enabled");
        anvilFilterEnabled = config.getBoolean("settings.filters.anvil.enabled");

        if (chatFilterEnabled) {
            getServer().getPluginManager().registerEvents(new ChatFilter(this), this);
        }
        if (signFilterEnabled) {
            getServer().getPluginManager().registerEvents(new SignFilter(this), this);
        }
        if (bookFilterEnabled) {
            getServer().getPluginManager().registerEvents(new BookFilter(this), this);
        }
        if (anvilFilterEnabled) {
            getServer().getPluginManager().registerEvents(new AnvilFilter(this), this);
        }
    }
}
