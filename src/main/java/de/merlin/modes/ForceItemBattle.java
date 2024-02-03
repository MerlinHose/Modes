package de.merlin.modes;

import de.merlin.Main;
import de.merlin.utils.GameMode;
import de.merlin.utils.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;
import java.util.List;

public class ForceItemBattle implements GameMode {

    private String name = "ForceItemBattle";
    private String description = "pick up items to get points";
    private Timer timer = new Timer();
    class GamePlayers {
        private Player player;
        private Material item;
        private int points;

        public GamePlayers(Player player) {
            this.player = player;
            this.item = Material.AIR;
            this.points = 0;
        }

        public Player getPlayer() {
            return player;
        }

        public Material getItem() {
            return item;
        }

        public void setItem(Material item) {
            this.item = item;
        }

        public int getPoints() {
            return points;
        }

        public void addPoints(int points) {
            this.points += points;
        }

        public void removePoints(int points) {
            this.points -= points;
        }
    }
    private List<GamePlayers> players = new ArrayList<GamePlayers>();
    private boolean running = false;
    private boolean paused = false;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void startGame(int min) {
        if(isRunning()) return;
        timer.start();
        running = true;
        paused = false;

        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(new GamePlayers(player));
        }

        for (GamePlayers player : players) {
            player.setItem(getLegitItem());
            player.getPlayer().sendMessage("Your Item: " + ChatColor.GREEN + player.getItem().name().toLowerCase().replace("_", " "));
        }

        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            if(isRunning()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(!isPaused()) {
                        player.sendActionBar(timer.getTimeString());

                        for (GamePlayers gamePlayer : players) {
                            if(gamePlayer.getPlayer().getInventory().contains(gamePlayer.getItem())) {
                                gamePlayer.addPoints(1);
                                gamePlayer.setItem(getLegitItem());
                                player.sendMessage("Next Item: " + ChatColor.GREEN + gamePlayer.getItem().name().toLowerCase().replace("_", " "));
                                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            }
                        }

                        if(timer.getElapsedTimeMins() >= min) {
                            stopGame();
                        }
                    } else {
                        player.sendActionBar("Paused");
                    }
                }
            }
        }, 0, 10);
    }

    private Material getLegitItem() {
        Material[] items = Material.values();
        Material item = items[(int) (Math.random() * items.length)];
        if(item.isItem()) {
            if(!(item.name().contains("SPAWN_EGG") || item.name().contains("COMMAND_BLOCK") || item.name().contains("STRUCTURE"))) {
                return item;
            } else {
                return getLegitItem();
            }
        } else {
            return getLegitItem();
        }
    }

    @Override
    public void stopGame() {
        if(!isRunning()) return;
        timer.stop();
        running = false;
        paused = false;

        getWinner();

        players.clear();

        Bukkit.getScheduler().cancelTasks(Main.plugin);
    }

    private void getWinner() {
        List<GamePlayers> playerRanked = new ArrayList<GamePlayers>();
        for (GamePlayers player : players) {
            playerRanked.add(player);
        }
        playerRanked.sort((o1, o2) -> o2.getPoints() - o1.getPoints());
        Bukkit.broadcastMessage("The winner is " + ChatColor.GREEN + playerRanked.get(0).getPlayer().getName() + ChatColor.WHITE + " with " + ChatColor.GREEN + playerRanked.get(0).getPoints() + ChatColor.WHITE + " points");
        for (GamePlayers player : playerRanked) {
            player.getPlayer().sendMessage("You got " + ChatColor.GREEN + player.getPoints() + ChatColor.WHITE + " points");
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_BELL_USE, 1, 1);
        }
    }

    @Override
    public void pauseGame() {
        if (isRunning() && !isPaused()) {
            timer.pause();
            paused = true;
        }
    }

    @Override
    public void resumeGame() {
        if (isRunning() && isPaused()) {
            timer.resume();
            paused = false;
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();

        if(isRunning() && !isPaused()) {
            for (GamePlayers gamePlayer : players) {
                if(gamePlayer.getPlayer().equals(player)) {
                    if(gamePlayer.getItem().equals(item.getItemStack().getType())) {
                        gamePlayer.addPoints(1);
                        gamePlayer.setItem(getLegitItem());
                        player.sendMessage("Next Item: " + ChatColor.GREEN + gamePlayer.getItem().name().toLowerCase().replace("_", " "));
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                }
            }
        }
    }
}
