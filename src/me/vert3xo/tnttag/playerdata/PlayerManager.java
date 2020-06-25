package me.vert3xo.tnttag.playerdata;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class PlayerManager implements Listener {
    private UUID uuid;
    private boolean inGame;
    private int coinsEarned;
    private boolean isDead;
    private boolean hasTNT;

    public PlayerManager(UUID uuid, boolean inGame, int coinsEarned, boolean isDead, boolean hasTNT) {
        this.uuid = uuid;
        this.inGame = inGame;
        this.coinsEarned = coinsEarned;
        this.isDead = isDead;
        this.hasTNT = hasTNT;
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

    public boolean isHasTNT() {
        return hasTNT;
    }

    public void setHasTNT(boolean hasTNT) {
        this.hasTNT = hasTNT;
        if (hasTNT) {
            this.makePlayerTNT(Bukkit.getPlayer(this.getUuid()));
        } else {
            this.removePlayerTNT(Bukkit.getPlayer(this.getUuid()));
        }
    }

    public void makePlayerTNT(Player player) {
        PlayerInventory pInv = player.getInventory();
        ItemStack tnt = new ItemStack(Material.TNT);
        pInv.setHelmet(tnt);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        player.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + " is IT!");
        pInv.addItem(tnt);
    }

    public void removePlayerTNT(Player player) {
        PlayerInventory pInv = player.getInventory();
        pInv.setHelmet(null);
        pInv.clear();
    }
}
