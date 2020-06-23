package me.vert3xo.tnttag.configuration;

import me.vert3xo.tnttag.files.LocationHandler;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationManager {
    public ConfigurationManager() {
        this.locationHandler();
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
        LocationHandler.get().options().copyDefaults(true);
        LocationHandler.save();
    }
}
