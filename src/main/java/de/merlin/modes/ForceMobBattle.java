package de.merlin.modes;

import de.merlin.Main;
import de.merlin.utils.GameMode;
import de.merlin.utils.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;
import java.util.List;

public class ForceMobBattle implements GameMode {

    private String name = "ForceEntityBattle";
    private String description = "kill entity to get points";
    private Timer timer = new Timer();
    class GamePlayers {
        private Player player;
        private EntityType entity;
        private int points;

        public GamePlayers(Player player) {
            this.player = player;
            this.entity = null;
            this.points = 0;
        }

        public Player getPlayer() {
            return player;
        }

        public EntityType getEntity() {
            return entity;
        }

        public void setEntity(EntityType entity) {
            this.entity = entity;
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
            player.setEntity(getLegitEntity());
            player.getPlayer().sendMessage("Your Entity: " + ChatColor.GREEN + player.getEntity().name().toString().toLowerCase().replace("_", " "));
        }

        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            if(isRunning()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(!isPaused()) {
                        player.sendActionBar(timer.getTimeString());
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

    private EntityType getLegitEntity() {
        EntityType[] mobs = {EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.WITCH, EntityType.ENDERMAN, EntityType.BLAZE, EntityType.GHAST, EntityType.SLIME, EntityType.MAGMA_CUBE, EntityType.WITHER_SKELETON, EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.IRON_GOLEM, EntityType.SNOWMAN};
        return mobs[(int) (Math.random() * mobs.length)];
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
    public void onEntityDeath(EntityDeathEvent event) {
        if(isRunning() && !isPaused()) {
            for (GamePlayers gamePlayer : players) {
                if(gamePlayer.getEntity().equals(event.getEntity().getType())) {
                    gamePlayer.addPoints(1);
                    gamePlayer.setEntity(getLegitEntity());
                    gamePlayer.getPlayer().sendMessage("Next Entity: " + ChatColor.GREEN + gamePlayer.getEntity().name().toString().toLowerCase().replace("_", " "));
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                }
            }
        }
    }
}
