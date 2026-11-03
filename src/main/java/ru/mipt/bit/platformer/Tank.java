package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class Tank implements MovableObject, GameObject {

    private static final int DEFAULT_HEALTH = 3;
    private static final float DEFAULT_RELOAD_DURATION = 0.5f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final float movementDuration;
    private final float reloadDuration;

    private float movementProgress = 1f;
    private float reloadTime;
    private Direction direction = Direction.RIGHT;
    private int health;

    public Tank(int x, int y, float movementDuration) {
        this(
                x,
                y,
                movementDuration,
                DEFAULT_HEALTH,
                DEFAULT_RELOAD_DURATION
        );
    }

    public Tank(
            int x,
            int y,
            float movementDuration,
            int health,
            float reloadDuration
    ) {
        if (!(movementDuration > 0f)) {
            throw new IllegalArgumentException(
                    "Movement duration must be positive"
            );
        }

        if (health <= 0) {
            throw new IllegalArgumentException(
                    "Health must be positive"
            );
        }

        if (!(reloadDuration > 0f)) {
            throw new IllegalArgumentException(
                    "Reload duration must be positive"
            );
        }

        coordinates = new GridPoint2(x, y);
        destinationCoordinates = new GridPoint2(coordinates);
        this.movementDuration = movementDuration;
        this.health = health;
        this.reloadDuration = reloadDuration;
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
        reloadTime = Math.max(0f, reloadTime - deltaTime);

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
        update(deltaTime);
    }

    public boolean canShoot() {
        return reloadTime == 0f && !isMoving() && isAlive();
    }

    public void reload() {
        reloadTime = reloadDuration;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException(
                    "Damage must not be negative"
            );
        }

        health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public Direction getDirection() {
        return direction;
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
