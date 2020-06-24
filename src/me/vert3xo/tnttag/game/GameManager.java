package me.vert3xo.tnttag.game;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.files.LocationHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class GameManager implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    private int lobbyCountdown = 10;
    private int explosionCountdown = 30;
    private int playersNeeded = 1;
    private boolean isStarted;
    private FileConfiguration locationConfig = LocationHandler.get();

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

    public void lobbyWait(Player player) {
        int online = plugin.getServer().getOnlinePlayers().size();
        plugin.getServer().broadcastMessage(ChatColor.GREEN + "We have enough players, the game is starting!");
        playersCheck(online);
    }

    public void gameStart() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.teleport(gameSpawn);
        }
        explosionCountdown();
    }

    public void gameStop() {
        // TODO: Implement BungeeCord
    }

    public void playersCheck(int online) {
        if (online == playersNeeded) {
            lobbyCountdown();
        }
    }

    public void explosionCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (explosionCountdown > 0) {
                        --explosionCountdown;
                    } else {
                        plugin.gameMechanics.tntCheck(p);
                        explosionCountdown = 30;
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 24);
    }

    public void lobbyCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (lobbyCountdown > 0) {
                    plugin.getServer().broadcastMessage(ChatColor.GREEN + "The game is starting in " + lobbyCountdown-- + " seconds.");
                } else {
                    setStarted(true);
                    gameStart();
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 24);
    }
}
