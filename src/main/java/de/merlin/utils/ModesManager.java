package de.merlin.utils;

import de.merlin.modes.ForceItemBattle;
import de.merlin.modes.ForceMobBattle;

import java.util.ArrayList;
import java.util.List;

public class ModesManager {

    private List<GameMode> gameModes = new ArrayList<>();
    public GameMode currentGameMode;

    public ModesManager() {
        gameModes.add(new ForceItemBattle());
        gameModes.add(new ForceMobBattle());
    }

    public List<GameMode> getGameModes() {
        return gameModes;
    }
}
