package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;

public enum Direction {

    UP(0, 1, 90f, Input.Keys.UP, Input.Keys.W),
    LEFT(-1, 0, -180f, Input.Keys.LEFT, Input.Keys.A),
    DOWN(0, -1, -90f, Input.Keys.DOWN, Input.Keys.S),
    RIGHT(1, 0, 0f, Input.Keys.RIGHT, Input.Keys.D);

    private final int xShift;
    private final int yShift;
    private final float rotation;
    private final int arrowKey;
    private final int letterKey;

    Direction(
            int xShift,
            int yShift,
            float rotation,
            int arrowKey,
            int letterKey
    ) {
        this.xShift = xShift;
        this.yShift = yShift;
        this.rotation = rotation;
        this.arrowKey = arrowKey;
        this.letterKey = letterKey;
    }

    public boolean isPressed() {
        return Gdx.input.isKeyPressed(arrowKey)
                || Gdx.input.isKeyPressed(letterKey);
    }

    public GridPoint2 destinationFrom(GridPoint2 coordinates) {
        return new GridPoint2(
                coordinates.x + xShift,
                coordinates.y + yShift
        );
    }

    public float getRotation() {
        return rotation;
    }
}
