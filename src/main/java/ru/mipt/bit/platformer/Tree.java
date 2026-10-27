package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class Tree implements PositionedObject, Obstacle {

    private final GridPoint2 coordinates;

    public Tree(int x, int y) {
        coordinates = new GridPoint2(x, y);
    }

    @Override
    public boolean occupies(GridPoint2 destination) {
        return coordinates.equals(destination);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }
}
