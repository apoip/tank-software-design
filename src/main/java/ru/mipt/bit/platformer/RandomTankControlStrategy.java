package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTankControlStrategy implements TankControlStrategy {

    private final Tank tank;
    private final List<Command> commands;
    private final Random random;

    public RandomTankControlStrategy(
            GameField field,
            Tank tank
    ) {
        this(field, tank, new Random());
    }

    RandomTankControlStrategy(
            GameField field,
            Tank tank,
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

        commands.add(new ShootTankCommand(field, tank));
    }

    @Override
    public List<Command> createCommands() {
        if (tank.isMoving() || !tank.isAlive()) {
            return java.util.Collections.emptyList();
        }

        return java.util.Collections.singletonList(
                commands.get(random.nextInt(commands.size()))
        );
    }
}
