package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameField {

    private final List<MovableObject> tanks;
    private final List<Obstacle> obstacles;
    private final int width;
    private final int height;

    public GameField(
            Collection<? extends MovableObject> tanks,
            Collection<? extends Obstacle> obstacles,
            int width,
            int height
    ) {
        this.tanks = new ArrayList<>(tanks);
        this.obstacles = new ArrayList<>(obstacles);
        this.width = width;
        this.height = height;
    }

    public void moveTank(
            MovableObject tank,
            Direction direction
    ) {
        if (tank.isMoving()) {
            return;
        }

        tank.turn(direction);

        GridPoint2 destination = direction.destinationFrom(
                tank.getCoordinates()
        );

        if (isInside(destination)
                && !isOccupiedByObstacle(destination)
                && !isOccupiedByTank(destination, tank)) {
            tank.startMoving(destination);
        }
    }

    private boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0
                && coordinates.x < width
                && coordinates.y >= 0
                && coordinates.y < height;
    }

    private boolean isOccupiedByObstacle(GridPoint2 coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return true;
            }
        }

        return false;
    }

    private boolean isOccupiedByTank(
            GridPoint2 coordinates,
            MovableObject movingTank
    ) {
        for (MovableObject tank : tanks) {
            if (tank == movingTank) {
                continue;
            }

            if (tank.getCoordinates().equals(coordinates)) {
                return true;
            }

            if (tank.isMoving()
                    && tank.getDestinationCoordinates()
                    .equals(coordinates)) {
                return true;
            }
        }

        return false;
    }

    public void update(float deltaTime) {
        for (MovableObject tank : tanks) {
            tank.update(deltaTime);
        }
    }
}
