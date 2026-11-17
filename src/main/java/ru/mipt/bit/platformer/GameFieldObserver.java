package ru.mipt.bit.platformer;

/**
 * Application output port for observing game-field changes.
 */
public interface GameFieldObserver {

    void objectAdded(GameObject object);

    void objectRemoved(GameObject object);
}
