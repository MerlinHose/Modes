package de.merlin;

import de.merlin.commands.ModesCommand;
import de.merlin.commands.ModesTab;
import de.merlin.modes.ForceItemBattle;
import de.merlin.utils.GameMode;
import de.merlin.utils.ModesManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main plugin;
    public ModesManager modesManager;

    @Override
    public void onEnable() {
        System.out.println("Plugin enabled");

        plugin = this;
        modesManager = new ModesManager();

        getCommand("modes").setExecutor(new ModesCommand());
        getCommand("modes").setTabCompleter(new ModesTab());

        PluginManager pm = getServer().getPluginManager();
        for (GameMode mode : modesManager.getGameModes()) {
            pm.registerEvents(mode, this);
        }
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin disabled");
    }
}
