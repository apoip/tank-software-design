package ru.mipt.bit.platformer;

import java.util.List;

/**
 * Application input port that produces commands for a tank.
 */
public interface TankControlStrategy {

    List<Command> createCommands();

    default void handle() {
        for (Command command : createCommands()) {
            command.execute();
        }
    }
}
