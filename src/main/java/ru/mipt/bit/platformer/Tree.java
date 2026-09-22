package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class Tree {

    private final GridPoint2 coordinates;

    public Tree(int x, int y) {
        coordinates = new GridPoint2(x, y);
    }

    public boolean occupies(GridPoint2 destination) {
        return coordinates.equals(destination);
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }
}