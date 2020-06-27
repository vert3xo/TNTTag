package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.configuration.LocationHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetArenaLocation extends MainCommandExecutor {
    public SetArenaLocation() {
        super("setarena", "tnttag.admin", false);
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        Player p = (Player) commandSender;
        Location playerLocation = p.getLocation();
        LocationHandler.get().set("arena-location.X", playerLocation.getX());
        LocationHandler.get().set("arena-location.Y", playerLocation.getY());
        LocationHandler.get().set("arena-location.Z", playerLocation.getZ());
        LocationHandler.get().set("arena-location.world", playerLocation.getWorld().getName());
        LocationHandler.save();
        commandSender.sendMessage(ChatColor.GREEN + "Arena location set!");
        return true;
    }
}
