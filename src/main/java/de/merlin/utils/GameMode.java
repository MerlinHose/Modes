package de.merlin.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public interface GameMode extends Listener {

    public String getName();
    public String getDescription();
    public Timer getTimer();
    public boolean isRunning();
    public boolean isPaused();
    public void startGame(int min);
    public void stopGame();
    public void pauseGame();
    public void resumeGame();
}