package me.vert3xo.tnttag;

import me.vert3xo.tnttag.commands.*;
import me.vert3xo.tnttag.configuration.ConfigurationManager;
import me.vert3xo.tnttag.events.GameMechanics;
import me.vert3xo.tnttag.game.GameManager;
import me.vert3xo.tnttag.playerdata.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {
    public HashMap<UUID, PlayerManager> playerManager = new HashMap<>();
    public ArrayList<Player> playersInGame = new ArrayList<>();
    public ArrayList<Player> playersLeftGame = new ArrayList<>();
    public ArrayList<Player> deadPlayers = new ArrayList<>();
    public GameMechanics gameMechanics;
    public GameManager gameManager;

    @Override
    public void onEnable() {
        this.loadConfigManager();
        this.instantiateClasses();
        this.registerEvents();
        this.registerCommands();
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "TNT Tag enabled.");
    }

    @Override
    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "TNT Tag disabled.");
    }



    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new GameMechanics(), this);
    }

    private void registerCommands() {
        new ForceStartGameCommand();
        new ForceStopGameCommand();
        new SetArenaLocation();
        new SetLobbyLocation();
        new ArenaTeleport();
        new LobbyTeleport();
    }

    private void loadConfigManager() {
        new ConfigurationManager();
    }

    private void instantiateClasses() {
        gameMechanics = new GameMechanics();
        gameManager = new GameManager();
        gameManager.setupGame();
    }
}
