package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class Tank implements MovableObject {

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final float movementDuration;

    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;

    public Tank(int x, int y, float movementDuration) {
        if (!(movementDuration > 0f)) {
            throw new IllegalArgumentException(
                    "Movement duration must be positive"
            );
        }

        coordinates = new GridPoint2(x, y);
        destinationCoordinates = new GridPoint2(coordinates);
        this.movementDuration = movementDuration;
    }

    @Override
    public boolean isMoving() {
        return movementProgress < 1f;
    }

    @Override
    public void turn(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void startMoving(GridPoint2 destination) {
        if (isMoving()) {
            return;
        }

        destinationCoordinates.set(destination);
        movementProgress = 0f;
    }

    @Override
    public void update(float deltaTime) {
        if (!isMoving()) {
            return;
        }

        movementProgress = Math.min(
                1f,
                movementProgress + deltaTime / movementDuration
        );

        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return direction.getRotation();
    }
}
