package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class Bullet implements GameObject {

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final Direction direction;
    private final float movementDuration;
    private final int damage;

    private float movementProgress = 1f;

    public Bullet(
            GridPoint2 coordinates,
            Direction direction,
            float movementDuration,
            int damage
    ) {
        if (!(movementDuration > 0f)) {
            throw new IllegalArgumentException(
                    "Movement duration must be positive"
            );
        }

        if (damage <= 0) {
            throw new IllegalArgumentException(
                    "Damage must be positive"
            );
        }

        this.coordinates = new GridPoint2(coordinates);
        destinationCoordinates = new GridPoint2(coordinates);
        this.direction = direction;
        this.movementDuration = movementDuration;
        this.damage = damage;
    }

    public void startMoving() {
        destinationCoordinates.set(
                direction.destinationFrom(coordinates)
        );
        movementProgress = 0f;
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    public void advance(float deltaTime) {
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
    public void live(float deltaTime, GameField field) {
        field.advanceBullet(this, deltaTime);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public Direction getDirection() {
        return direction;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public int getDamage() {
        return damage;
    }
}
