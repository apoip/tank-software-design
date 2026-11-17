package ru.mipt.bit.platformer;

/**
 * Domain state for a tank with 15 to 70 percent health.
 */
public final class MediumTankState implements TankState {

    private static final float MOVEMENT_SLOWDOWN = 2f;

    @Override
    public float getMovementDuration(float baseMovementDuration) {
        return baseMovementDuration * MOVEMENT_SLOWDOWN;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
