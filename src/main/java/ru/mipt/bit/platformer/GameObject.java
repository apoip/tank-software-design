package ru.mipt.bit.platformer;

public interface GameObject extends PositionedObject {

    void live(float deltaTime, GameField field);
}
