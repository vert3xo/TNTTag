package me.vert3xo.tnttag.game;

import me.vert3xo.tnttag.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class GameManager implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    private int lobbyCountdown = 10;
    private int explosionCountdown = 30;
    private int playersNeeded = 1;
    private boolean isStarted;

    Location lobbySpawn;
    Location gameSpawn;

    public void setupGame() {
        
    }

    public void lobbyWait(Player player) {

    }

    public void gameStart() {

    }

    public void gameStop() {
        // TODO: Implement BungeeCord
    }

    public void explosionCountdown() {

    }

    public void lobbyCountdown() {

    }
}
