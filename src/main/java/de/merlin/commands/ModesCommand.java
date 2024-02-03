package de.merlin.commands;

import de.merlin.Main;
import de.merlin.utils.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ModesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if(strings.length < 1) {
                player.sendMessage("Usage: /modes <list|start|stop|pause|resume> [mode] [min]");
            } else if(strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
                player.sendMessage("Modes:");
                for (GameMode gameMode : Main.plugin.modesManager.getGameModes()) {
                    player.sendMessage(" - " + gameMode.getName() + ", " + gameMode.getDescription());
                }
            } else if(strings.length == 1 && strings[0].equalsIgnoreCase("status")) {
                for (GameMode gameMode : Main.plugin.modesManager.getGameModes()) {
                    player.sendMessage(gameMode.getName() + ": " + (gameMode.isRunning() ? "running" : "stopped") + (gameMode.isPaused() ? ", paused" : ""));
                }
            } else if(strings.length == 2) {
                switch (strings[0]) {
                    case "stop":
                        for(GameMode gameMode : Main.plugin.modesManager.getGameModes()) {
                            if(gameMode.getName().equalsIgnoreCase(strings[1])) {
                                gameMode.stopGame();
                                break;
                            }
                        }
                        break;
                    case "pause":
                        for(GameMode gameMode : Main.plugin.modesManager.getGameModes()) {
                            if(gameMode.getName().equalsIgnoreCase(strings[1])) {
                                gameMode.pauseGame();
                                break;
                            }
                        }
                        break;
                    case "resume":
                        for(GameMode gameMode : Main.plugin.modesManager.getGameModes()) {
                            if(gameMode.getName().equalsIgnoreCase(strings[1])) {
                                gameMode.resumeGame();
                                break;
                            }
                        }
                        break;
                }
            } else if(strings.length == 3 && strings[0].equalsIgnoreCase("start")) {
                for(GameMode gameMode : Main.plugin.modesManager.getGameModes()) {
                    if(gameMode.getName().equalsIgnoreCase(strings[1])) {
                        if(Main.plugin.modesManager.currentGameMode != null) {
                            Main.plugin.modesManager.currentGameMode.stopGame();
                            Main.plugin.modesManager.currentGameMode = null;
                        }
                        try {
                            gameMode.startGame(Integer.parseInt(strings[2]));
                            Main.plugin.modesManager.currentGameMode = gameMode;
                        } catch (NumberFormatException e) {
                            player.sendMessage("Invalid number: " + strings[2]);
                        }
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
