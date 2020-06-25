package me.vert3xo.tnttag.commands;

import me.vert3xo.tnttag.game.GameManager;
import org.bukkit.command.CommandSender;

public class ForceStartGameCommand extends MainCommandExecutor {
    public ForceStartGameCommand() {
        super("forcestart", "tnttag.admin", true);
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        GameManager gm = new GameManager();
        gm.setupGame();
        gm.setStarted(true);
        gm.gameStart();
        return true;
    }
}
