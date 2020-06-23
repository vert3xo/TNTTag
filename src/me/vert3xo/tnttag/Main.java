package me.vert3xo.tnttag;

import me.vert3xo.tnttag.events.GameMechanics;
import me.vert3xo.tnttag.commands.StartGameCommand;
import me.vert3xo.tnttag.playerdata.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {
    public HashMap<UUID, PlayerManager> playerManager = new HashMap<>();

    @Override
    public void onEnable() {
        this.loadConfig();
        this.registerEvents();
        this.registerCommands();
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "TNT Tag enabled.");
    }

    @Override
    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "TNT Tag disabled.");
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new GameMechanics(), this);
    }

    private void registerCommands() {
        new StartGameCommand();
    }

    private void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}
