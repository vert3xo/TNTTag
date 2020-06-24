package me.vert3xo.tnttag.events;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.configuration.ConfigurationManager;
import me.vert3xo.tnttag.files.LocationHandler;
import me.vert3xo.tnttag.playerdata.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class GameMechanics implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        FileConfiguration config = ConfigurationManager.getConfig();
        FileConfiguration locationConfig = LocationHandler.get();
        Player p = e.getPlayer();
        if (plugin.getServer().getOnlinePlayers().size() >= config.getInt("max-players")) {
            // TODO: Setup BungeeCord fallback server
            p.kickPlayer(ChatColor.RED + "Maximum number of players reached.");
            plugin.getServer().broadcastMessage(ChatColor.RED + p.getDisplayName() + " has been kicked! Maximum number of players reached.");
        }
        UUID uuid = p.getUniqueId();
        this.plugin.playerManager.put(uuid, new PlayerManager(uuid, false, 0, false));
        this.plugin.gameManager.lobbyWait(p);
        if (!(p.hasPermission("tnttag.admin"))) {
            Location lobby = new Location(
                    plugin.getServer().getWorld(locationConfig.getString("lobby-location.world")),
                    locationConfig.getDouble("lobby-location.X"),
                    locationConfig.getDouble("lobby-location.Y"),
                    locationConfig.getDouble("lobby-location.Z")
            );
            p.teleport(lobby);
        }
        e.setJoinMessage(ChatColor.YELLOW + p.getDisplayName() + " joined (" + ChatColor.AQUA + plugin.getServer().getOnlinePlayers().size() + ChatColor.YELLOW + "/" + ChatColor.AQUA + config.getInt("max-players") + ChatColor.YELLOW + ")");
    }

    public void tntCheck(Player player) {

    }
}
