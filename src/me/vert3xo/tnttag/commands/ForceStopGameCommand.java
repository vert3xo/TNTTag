package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.game.GameManager;
import org.bukkit.command.CommandSender;

public class ForceStopGameCommand extends MainCommandExecutor {
    public ForceStopGameCommand() {
        super("forcestop", "tnttag.admin", true);
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        GameManager gm = new GameManager();
        gm.setStarted(false);
        gm.gameStop();
        return true;
    }
}
