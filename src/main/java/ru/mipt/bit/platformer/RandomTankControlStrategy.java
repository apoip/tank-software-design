package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTankControlStrategy implements TankControlStrategy {

    private final MovableObject tank;
    private final List<Command> commands;
    private final Random random;

    public RandomTankControlStrategy(
            GameField field,
            MovableObject tank
    ) {
        this(field, tank, new Random());
    }

    RandomTankControlStrategy(
            GameField field,
            MovableObject tank,
            Random random
    ) {
        this.tank = tank;
        this.random = random;
        commands = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            commands.add(
                    new MoveTankCommand(field, tank, direction)
            );
        }
    }

    @Override
    public void handle() {
        if (tank.isMoving()) {
            return;
        }

        commands.get(random.nextInt(commands.size())).execute();
    }
}
