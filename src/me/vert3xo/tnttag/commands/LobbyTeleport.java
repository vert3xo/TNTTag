package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.files.LocationHandler;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LobbyTeleport extends MainCommandExecutor {
    public LobbyTeleport() {
        super("lobbytp", "tnttag.admin", false);
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        FileConfiguration locationConfig = LocationHandler.get();
        Main plugin = Main.getPlugin(Main.class);
        Location location = new Location(plugin.getServer().getWorld(locationConfig.getString("lobby-location.world")), locationConfig.getDouble("lobby-location.X"), locationConfig.getDouble("lobby-location.Y"), locationConfig.getDouble("lobby-location.Z"));
        ((Player) commandSender).teleport(location);
        return true;
    }
}
