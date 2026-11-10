package ru.mipt.bit.platformer;

import java.util.List;

public interface TankControlStrategy {

    List<Command> createCommands();

    default void handle() {
        for (Command command : createCommands()) {
            command.execute();
        }
    }
}
