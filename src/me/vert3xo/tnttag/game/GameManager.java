package me.vert3xo.tnttag.game;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.configuration.ConfigurationManager;
import me.vert3xo.tnttag.configuration.LocationHandler;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class GameManager implements Listener {
    private Main plugin = Main.getPlugin(Main.class);
    private FileConfiguration config = ConfigurationManager.getConfig();

    private int lobbyCountdown = config.getInt("lobby-wait-time");
    private int explosionCountdown = config.getInt("explosion-timer");
    private int gameEndTimer = config.getInt("game-end-wait-time");
    private int playersNeeded = config.getInt("needed-players");
    public Player winner = null;
    private boolean isStarted;
    private FileConfiguration locationConfig = LocationHandler.get();
    private int roundsPassed = 0;
    public ArrayList<Player> tnts = new ArrayList<>();

    Location lobbySpawn;
    Location gameSpawn;

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public void setupGame() {
        this.gameSpawn = new Location(
                plugin.getServer().getWorld(locationConfig.getString("arena-location.world")),
                locationConfig.getDouble("arena-location.X"),
                locationConfig.getDouble("arena-location.Y"),
                locationConfig.getDouble("arena-location.Z")
        );
        this.lobbySpawn = new Location(
                plugin.getServer().getWorld(locationConfig.getString("lobby-location.world")),
                locationConfig.getDouble("lobby-location.X"),
                locationConfig.getDouble("lobby-location.Y"),
                locationConfig.getDouble("lobby-location.Z")
        );
    }

    public void lobbyWait() {
        if (plugin.playersInGame.size() == playersNeeded) {
            this.lobbyCountdown();
        }
    }

    public void lobbyCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (lobbyCountdown > 0) {
                    lobbyCountdown--;
                    plugin.getServer().broadcastMessage(ChatColor.GREEN + "Game starting in " + lobbyCountdown);
                    if (!(plugin.playersInGame.size() >= playersNeeded)) {
                        this.cancel();
                    }
                } else {
                    gameStart();
                    lobbyCountdown = config.getInt("lobby-wait-time");
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 24);
    }

    public void explosionCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (explosionCountdown > 0) {
                    explosionCountdown--;
//                    plugin.getServer().broadcastMessage(String.valueOf(explosionCountdown));
                } else {
                    for (Player p : tnts) {
                        plugin.deadPlayers.add(p);
                    }
                    plugin.gameMechanics.tntCheck();
                    tnts.clear();
                    makeDeadSpectators();
                    if (plugin.playersInGame.size() + tnts.size() == 1) {
                        winner = plugin.playersInGame.get(0);
                        gameEndCountdown();
                        this.cancel();
                    }
                    tnts = chooseRandomTNTs();
                    explosionCountdown = config.getInt("explosion-timer");
                }
            }
        }.runTaskTimer(plugin, 0, 24);
    }

    public void gameEndCountdown() {
        if (winner != null) {
            plugin.getServer().broadcastMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Game ended, the winner is " + winner.getDisplayName() + "!");
        } else {
            plugin.getServer().broadcastMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Game ended, congratulations to the winner!");
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameEndTimer > 0) {
                    gameEndTimer--;
//                    plugin.getServer().broadcastMessage(String.valueOf(gameEndTimer));
                } else {
                    gameStop();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 24);
    }

    public void gameStart() {
        this.setStarted(true);
        if (plugin.playersInGame.size() == 1) {
            winner = plugin.playersInGame.get(0);
            winner.teleport(this.gameSpawn);
            this.gameEndCountdown();
            return;
        } else if (plugin.playersInGame.size() == 0) {
            this.gameEndCountdown();
            return;
        }
        this.setStarted(true);
        for (Player p : plugin.playersInGame) {
            p.teleport(this.gameSpawn);
        }
        tnts = chooseRandomTNTs();
        for (Player p : tnts) {
            plugin.playerManager.get(p.getUniqueId()).setHasTNT(true);
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "You are IT!");
        }
        this.explosionCountdown();
    }

    public void gameStop() {
        // TODO: Implement BungeeCord
        for(Player p : plugin.getServer().getOnlinePlayers()) {
            if (!(p.hasPermission("tnttag.admin"))) {
                p.kickPlayer(ChatColor.GREEN + "Game ended, thanks for playing!");
            }
        }
        this.setStarted(false);
    }

    private ArrayList<Player> chooseRandomTNTs() {
        ArrayList<Player> tnts = new ArrayList<>();
        ArrayList<Player> playersNotChosen = plugin.playersInGame;
        int numberOfTNTs = (int) (Math.ceil(30f / 100f * (float) playersNotChosen.size()));
        for(int i = 0; i < numberOfTNTs; i++) {
            int randomNumber = new Random().nextInt(playersNotChosen.size());
            tnts.add(playersNotChosen.get(randomNumber));
            playersNotChosen.remove(randomNumber);
        }
        return tnts;
    }

    private void makeDeadSpectators() {
        for (Player p : plugin.deadPlayers) {
            PlayerInventory pInv = p.getInventory();
            pInv.clear();
            pInv.setHelmet(null);
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    public Player checkForWinner() {
        if (plugin.playersInGame.size() + tnts.size() == 1) {
            if (plugin.playersInGame.size() == 1) {
                return plugin.playersInGame.get(0);
            } else {
                plugin.playerManager.get(tnts.get(0).getUniqueId()).setHasTNT(false);
                return tnts.get(0);
            }
        }
        return null;
    }
}
