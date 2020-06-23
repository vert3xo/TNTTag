package me.vert3xo.tnttag.playerdata;

import org.bukkit.event.Listener;

import java.util.UUID;

public class PlayerManager implements Listener {
    private UUID uuid;
    private boolean inGame;
    private int coinsEarned;
    private boolean isDead;

    public PlayerManager(UUID uuid, boolean inGame, int coinsEarned, boolean isDead) {
        this.uuid = uuid;
        this.inGame = inGame;
        this.coinsEarned = coinsEarned;
        this.isDead = isDead;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public void setCoinsEarned(int coinsEarned) {
        this.coinsEarned = coinsEarned;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
