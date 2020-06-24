package me.vert3xo.tnttag.events;

import me.vert3xo.tnttag.Main;
import me.vert3xo.tnttag.playerdata.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class GameMechanics implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        this.plugin.playerManager.put(uuid, new PlayerManager(uuid, false, 0, false));
        this.plugin.gameManager.lobbyWait(p);
    }

    public void tntCheck(Player player) {

    }
}
