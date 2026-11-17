package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain interface for an object with field coordinates.
 */
public interface PositionedObject {

    GridPoint2 getCoordinates();
}
