package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.files.LocationHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyLocation extends MainCommandExecutor {
    public SetLobbyLocation() {
        super("setlobby", "tnttag.admin", false);
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        Player p = (Player) commandSender;
        Location playerLocation = p.getLocation();
        LocationHandler.get().set("lobby-location.X", playerLocation.getX());
        LocationHandler.get().set("lobby-location.Y", playerLocation.getY());
        LocationHandler.get().set("lobby-location.Z", playerLocation.getZ());
        LocationHandler.get().set("lobby-location.world", playerLocation.getWorld().getName());
        LocationHandler.save();
        commandSender.sendMessage(ChatColor.GREEN + "Lobby location set!");
        return true;
    }
}
