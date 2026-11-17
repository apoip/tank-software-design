package ru.mipt.bit.platformer;

/**
 * Application use case for moving a tank.
 */
public class MoveTankCommand implements Command {

    private final GameField field;
    private final MovableObject tank;
    private final Direction direction;

    public MoveTankCommand(
            GameField field,
            MovableObject tank,
            Direction direction
    ) {
        this.field = field;
        this.tank = tank;
        this.direction = direction;
    }

    @Override
    public void execute() {
        field.moveTank(tank, direction);
    }
}
