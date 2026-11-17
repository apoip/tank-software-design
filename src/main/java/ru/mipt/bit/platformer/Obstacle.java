package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain interface for an object that occupies a field cell.
 */
public interface Obstacle {

    boolean occupies(GridPoint2 coordinates);
}
