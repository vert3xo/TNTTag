package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.files.LocationHandler;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ArenaTeleport extends MainCommandExecutor {
    public ArenaTeleport() {
        super("arenatp", "tnttag.admin", false);
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        FileConfiguration locationConfig = LocationHandler.get();
        Main plugin = Main.getPlugin(Main.class);
        Location location = new Location(plugin.getServer().getWorld(locationConfig.getString("arena-location.world")), locationConfig.getDouble("arena-location.X"), locationConfig.getDouble("arena-location.Y"), locationConfig.getDouble("arena-location.Z"));
        ((Player) commandSender).teleport(location);
        return true;
    }
}
