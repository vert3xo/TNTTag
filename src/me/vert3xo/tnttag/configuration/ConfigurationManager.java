package me.vert3xo.tnttag.configuration;

import me.vert3xo.tnttag.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {
    public ConfigurationManager() {
        this.basicConfigHandler();
        this.locationHandler();
    }

    private Main plugin = Main.getPlugin(Main.class);
    private File dataFolder = plugin.getDataFolder();
    private static FileConfiguration config;

    private void basicConfigHandler() {
        if (!(dataFolder.exists())) {
            dataFolder.mkdir();
        }
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        config.addDefault("max-players", 20);
        config.addDefault("needed-players", 2);
        config.addDefault("lobby-wait-time", 10);
        config.addDefault("explosion-timer", 30);
        config.addDefault("game-end-wait-time", 20);
        config.options().copyDefaults(true);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    private void locationHandler() {
        LocationHandler.setup();
        LocationHandler.get().addDefault("lobby-location.X", 0);
        LocationHandler.get().addDefault("lobby-location.Y", 63);
        LocationHandler.get().addDefault("lobby-location.Z", 0);
        LocationHandler.get().addDefault("lobby-location.world", "world");
        LocationHandler.get().addDefault("arena-location.X", 0);
        LocationHandler.get().addDefault("arena-location.Y", 63);
        LocationHandler.get().addDefault("arena-location.Z", 0);
        LocationHandler.get().addDefault("arena-location.world", "world");
        LocationHandler.get().options().copyDefaults(true);
        LocationHandler.save();
    }
}
