package ru.mipt.bit.platformer;

/**
 * Domain state for a tank with less than 15 percent health.
 */
public final class HeavyTankState implements TankState {

    private static final float MOVEMENT_SLOWDOWN = 3f;

    @Override
    public float getMovementDuration(float baseMovementDuration) {
        return baseMovementDuration * MOVEMENT_SLOWDOWN;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
