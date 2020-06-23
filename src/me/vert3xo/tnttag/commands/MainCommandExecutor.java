package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MainCommandExecutor implements CommandExecutor {
    private String command;
    private String permission;
    private boolean canConsoleUse = false;

    public MainCommandExecutor(String command, String permission, boolean canConsoleUse) {
        this.command = command;
        this.permission = permission;
        this.canConsoleUse = canConsoleUse;
        this.setExecutor();
    }

    public MainCommandExecutor(String command, String permission) {
        this.command = command;
        this.permission = permission;
        this.setExecutor();
    }

    public abstract boolean execute(CommandSender commandSender, String[] args);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender.hasPermission(permission))) {
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
            return true;
        } else if (!(commandSender instanceof Player) && !(this.canConsoleUse)) {
            commandSender.sendMessage(ChatColor.RED + "only players are allowed to run this command.");
            return true;
        } else {
            return execute(commandSender, args);
        }
    }

    public void setExecutor() {
        Main.getPlugin(Main.class).getCommand(command).setExecutor(this);
    }
}
