package ru.mipt.bit.platformer;

/**
 * Domain state for a tank with more than 70 percent health.
 */
public final class LightTankState implements TankState {

    @Override
    public float getMovementDuration(float baseMovementDuration) {
        return baseMovementDuration;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
