package ru.mipt.bit.platformer;

public interface GameFieldObserver {

    void objectAdded(GameObject object);

    void objectRemoved(GameObject object);
}
