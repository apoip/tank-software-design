package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class GameField {

    private final Tank tank;
    private final Tree tree;

    public GameField() {
        this(new Tank(1, 1), new Tree(1, 3));
    }

    public GameField(Tank tank, Tree tree) {
        this.tank = tank;
        this.tree = tree;
    }

    public void moveTank(Direction direction) {
        if (tank.isMoving()) {
            return;
        }

        tank.turn(direction);

        GridPoint2 destination = direction.destinationFrom(
                tank.getCoordinates()
        );

        if (!tree.occupies(destination)) {
            tank.startMoving(destination);
        }
    }

    public void update(float deltaTime) {
        tank.update(deltaTime);
    }

    public Tank getTank() {
        return tank;
    }

    public Tree getTree() {
        return tree;
    }
}