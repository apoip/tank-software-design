package ru.mipt.bit.platformer;

/**
 * Domain state that defines movement and shooting restrictions for a tank.
 */
public interface TankState {

    float getMovementDuration(float baseMovementDuration);

    boolean canShoot();
}
