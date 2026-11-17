package ru.mipt.bit.platformer;

/**
 * Domain interface for an object that participates in the game loop.
 */
public interface GameObject extends PositionedObject {

    void live(float deltaTime, GameField field);
}
