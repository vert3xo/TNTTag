package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.configuration.LocationHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LobbyTeleport extends MainCommandExecutor {
    public LobbyTeleport() {
        super("lobbytp", "tnttag.admin", true);
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        Main plugin = Main.getPlugin(Main.class);
        FileConfiguration locationConfig = LocationHandler.get();
        Location location = new Location(plugin.getServer().getWorld(locationConfig.getString("lobby-location.world")), locationConfig.getDouble("lobby-location.X"), locationConfig.getDouble("lobby-location.Y"), locationConfig.getDouble("lobby-location.Z"));
        if (commandSender instanceof Player && ArrayUtils.isEmpty(args)) {
            ((Player) commandSender).teleport(location);
        } else if (!(ArrayUtils.isEmpty(args))) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (p.getDisplayName().equalsIgnoreCase(args[0])) {
                    p.teleport(location);
                    commandSender.sendMessage(ChatColor.GREEN + p.getDisplayName() + " has been teleported.");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "No such player found!");
                }
            }
        } else {
            commandSender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Error");
        }
        return true;
    }
}
