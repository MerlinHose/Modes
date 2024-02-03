package de.merlin.commands;

import de.merlin.Main;
import de.merlin.utils.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModesTab implements TabCompleter {

    private final String[] subCommands = {"list", "start", "stop", "pause", "resume", "status"};

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1) {
            return List.of(subCommands);
        } else if(strings.length == 2) {
            if(strings[0].equalsIgnoreCase("start") || strings[0].equalsIgnoreCase("stop") || strings[0].equalsIgnoreCase("pause") || strings[0].equalsIgnoreCase("resume")) {
                List<String> gameModes = Main.plugin.modesManager.getGameModes().stream().map(GameMode::getName).toList();
                return gameModes;
            }
        } else if(strings.length == 3) {
            if(strings[0].equalsIgnoreCase("start")) {
                return List.of("[min]");
            }
        }
        return null;
    }
}
