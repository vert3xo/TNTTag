package me.vert3xo.tnttag.events;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.configuration.ConfigurationManager;
import me.vert3xo.tnttag.configuration.LocationHandler;
import me.vert3xo.tnttag.playerdata.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class GameMechanics implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!(p.hasPermission("tnttag.admin"))) {
            p.setGameMode(GameMode.SURVIVAL);
        }
        if (!(plugin.gameManager.isStarted()) || p.hasPermission("tnttag.admin") || plugin.playersLeftGame.contains(p)) {
            if (plugin.playersLeftGame.contains(p)) {
                plugin.playersLeftGame.remove(p);
                if (plugin.playerManager.get(p.getUniqueId()).isHasTNT()) {
                    plugin.gameManager.tnts.add(p);
                } else {
                    plugin.playersInGame.add(p);
                }
            }
            FileConfiguration config = ConfigurationManager.getConfig();
            FileConfiguration locationConfig = LocationHandler.get();
            if (plugin.getServer().getOnlinePlayers().size() >= config.getInt("max-players")) {
                // TODO: Setup BungeeCord fallback server
                p.kickPlayer(ChatColor.RED + "Maximum number of players reached.");
                plugin.getServer().broadcastMessage(ChatColor.RED + p.getDisplayName() + " has been kicked! Maximum number of players reached.");
            }
            e.setJoinMessage(ChatColor.YELLOW + p.getDisplayName() + " joined (" + ChatColor.AQUA + plugin.getServer().getOnlinePlayers().size() + ChatColor.YELLOW + "/" + ChatColor.AQUA + config.getInt("max-players") + ChatColor.YELLOW + ")");
            UUID uuid = p.getUniqueId();
            this.plugin.playerManager.put(uuid, new PlayerManager(uuid, false, 0, false, false));
            this.plugin.playerManager.get(uuid).setHasTNT(false);
            this.plugin.playersInGame.add(p);
            this.plugin.gameManager.lobbyWait();
            if (!(p.hasPermission("tnttag.admin"))) {
                Location lobby = new Location(
                        plugin.getServer().getWorld(locationConfig.getString("lobby-location.world")),
                        locationConfig.getDouble("lobby-location.X"),
                        locationConfig.getDouble("lobby-location.Y"),
                        locationConfig.getDouble("lobby-location.Z")
                );
                p.teleport(lobby);
            }
        } else {
            p.kickPlayer(ChatColor.RED + "The game has already started.");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (plugin.playersInGame.size() != 0) {
            Player p = e.getPlayer();
            if (plugin.playerManager.get(p.getUniqueId()).isHasTNT()) {
                plugin.gameManager.tnts.remove(p);
            } else {
                plugin.playersInGame.remove(p);
            }
            System.out.println("\n\n\n" + String.valueOf(plugin.playersInGame.size() + plugin.gameManager.tnts.size()));
            plugin.playersLeftGame.add(p);
            plugin.gameManager.winner = plugin.gameManager.checkForWinner();
            if (plugin.gameManager.winner != null) {
                plugin.gameManager.gameEndCountdown();
            }
        }
    }

    @EventHandler
    public void playerTNTTag(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (!plugin.gameManager.isStarted()) {
                e.setCancelled(true);
            }
            e.setDamage(0);
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                Player tagger = (Player) e.getDamager();
                UUID taggerUUID = tagger.getUniqueId();
                PlayerManager taggerPlayerManager = plugin.playerManager.get(taggerUUID);

                Player tagged = (Player) e.getEntity();
                UUID taggedUUID = tagged.getUniqueId();
                PlayerManager taggedPlayerManager = plugin.playerManager.get(taggedUUID);

                if (taggerPlayerManager.isHasTNT() && !(taggedPlayerManager.isHasTNT())) {
                    taggerPlayerManager.setHasTNT(false);
                    plugin.gameManager.tnts.remove(tagger);
                    plugin.playersInGame.add(tagger);

                    taggedPlayerManager.setHasTNT(true);
                    plugin.gameManager.tnts.add(tagged);
                    plugin.playersInGame.remove(tagged);
                }
            }
        } else if (!(e.getDamager() instanceof Player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!(e.getPlayer().hasPermission("tnttag.admin"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!(e.getPlayer().hasPermission("tnttag.admin"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void feedPlayers(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
        if (e instanceof Player) {
            ((Player) e).setHealth(20);
        }
    }

    public void tntCheck() {
        for (Player tnt : plugin.gameManager.tnts) {
            plugin.playersInGame.remove(tnt);
            plugin.deadPlayers.add(tnt);
        }
    }
}
