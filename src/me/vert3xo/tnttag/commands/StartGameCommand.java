package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.commands.MainCommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGameCommand extends MainCommandExecutor {
    public StartGameCommand() {
        super("start", "tnttag.admin", false);
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        return false;
    }
}
