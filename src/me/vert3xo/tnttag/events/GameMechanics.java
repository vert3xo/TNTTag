package me.vert3xo.tnttag.events;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.configuration.ConfigurationManager;
import me.vert3xo.tnttag.files.LocationHandler;
import me.vert3xo.tnttag.playerdata.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class GameMechanics implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!(plugin.gameManager.isStarted()) || p.hasPermission("tnttag.admin")) {
            FileConfiguration config = ConfigurationManager.getConfig();
            FileConfiguration locationConfig = LocationHandler.get();
            if (plugin.getServer().getOnlinePlayers().size() >= config.getInt("max-players")) {
                // TODO: Setup BungeeCord fallback server
                p.kickPlayer(ChatColor.RED + "Maximum number of players reached.");
                plugin.getServer().broadcastMessage(ChatColor.RED + p.getDisplayName() + " has been kicked! Maximum number of players reached.");
            }
            UUID uuid = p.getUniqueId();
            this.plugin.playerManager.put(uuid, new PlayerManager(uuid, false, 0, false, false));
            this.plugin.playersInGame.add(p);
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
        } else {
            p.kickPlayer(ChatColor.RED + "The game has already started.");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        plugin.playersInGame.remove(p);
        plugin.playersLeftGame.add(p);
        plugin.playerManager.remove(p.getUniqueId());
    }

    @EventHandler
    public void playerTNTTag(EntityDamageByEntityEvent e) {
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
                PlayerInventory taggerInventory = tagger.getInventory();
                taggerInventory.setHelmet(null);
                taggerInventory.clear();

                PlayerInventory taggedInventory = tagged.getInventory();
                ItemStack tnt = new ItemStack(Material.TNT);
                taggedInventory.setHelmet(tnt);
                tagged.playSound(tagged.getLocation(), Sound.LEVEL_UP, 1, 1);
                plugin.getServer().broadcastMessage(ChatColor.RED + tagged.getDisplayName() + " is IT!");
                taggedInventory.addItem(tnt);
            }
        }
    }

    @EventHandler
    public void HealFeedPlayer(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            p.setHealth(20);
            p.setFoodLevel(20);
        }
    }

    public void tntCheck(Player player) {

    }
}
