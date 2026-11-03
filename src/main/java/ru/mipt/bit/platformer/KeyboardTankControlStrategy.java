package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.List;

public class KeyboardTankControlStrategy implements TankControlStrategy {

    private final InputHandler inputHandler;
    private final List<Command> createdCommands;

    public KeyboardTankControlStrategy(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        createdCommands = new ArrayList<>();
    }

    public void register(Command command, int... keyCodes) {
        inputHandler.register(
                () -> createdCommands.add(command),
                keyCodes
        );
    }

    @Override
    public List<Command> createCommands() {
        createdCommands.clear();
        inputHandler.handle();
        return new ArrayList<>(createdCommands);
    }
}
