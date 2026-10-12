package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameField {

    private final MovableObject tank;
    private final List<Obstacle> obstacles;

    public GameField(
            MovableObject tank,
            Collection<? extends Obstacle> obstacles
    ) {
        this.tank = tank;
        this.obstacles = new ArrayList<>(obstacles);
    }

    public void moveTank(Direction direction) {
        if (tank.isMoving()) {
            return;
        }

        tank.turn(direction);

        GridPoint2 destination = direction.destinationFrom(
                tank.getCoordinates()
        );

        if (!isOccupied(destination)) {
            tank.startMoving(destination);
        }
    }

    private boolean isOccupied(GridPoint2 coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return true;
            }
        }

        return false;
    }

    public void update(float deltaTime) {
        tank.update(deltaTime);
    }
}
