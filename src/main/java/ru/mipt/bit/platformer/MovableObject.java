package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public interface MovableObject extends PositionedObject {

    boolean isMoving();

    void turn(Direction direction);

    void startMoving(GridPoint2 destination);

    void update(float deltaTime);

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    float getRotation();
}
