package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class Tank {

    private static final float MOVEMENT_DURATION = 0.4f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;

    public Tank(int x, int y) {
        coordinates = new GridPoint2(x, y);
        destinationCoordinates = new GridPoint2(coordinates);
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    public void turn(Direction direction) {
        this.direction = direction;
    }

    public void startMoving(GridPoint2 destination) {
        if (isMoving()) {
            return;
        }

        destinationCoordinates.set(destination);
        movementProgress = 0f;
    }

    public void update(float deltaTime) {
        if (!isMoving()) {
            return;
        }

        movementProgress = Math.min(
                1f,
                movementProgress + deltaTime / MOVEMENT_DURATION
        );

        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return direction.getRotation();
    }
}